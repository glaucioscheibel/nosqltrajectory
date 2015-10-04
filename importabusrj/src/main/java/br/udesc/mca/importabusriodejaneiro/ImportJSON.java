package br.udesc.mca.importabusriodejaneiro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImportJSON implements Job {
    private static final String URL = "http://dadosabertos.rio.rj.gov.br/apiTransporte/apresentacao/rest/index.cfm/onibus";
    private static Map<String, Long> buses = new HashMap<>();

    public void importa() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL);
        request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml,application/json");
        HttpResponse result = httpClient.execute(request);
        HttpEntity entity = result.getEntity();
        String json = EntityUtils.toString(entity, "UTF-8");
        httpClient.close();
        System.out.println(json.getBytes().length);
        ObjectMapper om = new ObjectMapper();
        BusData bd = om.readValue(json, BusData.class);
        for (List<String> ls : bd.getData()) {
            long ts = sdf.parse(ls.get(0)).getTime();
            String busid = ls.get(1);
            if (buses.containsKey(busid)) {
                if (ts <= buses.get(busid)) {
                    continue;
                }
            } else {
                buses.put(busid, ts);
            }
            String pre = ls.get(1).substring(0, 2);
            File dir = new File("/busrj/" + pre + "/");
            dir.mkdirs();
            File bus = new File(dir, ls.get(1) + ".csv");
            FileWriter fw = new FileWriter(bus, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(ls.get(0));
            bw.write(',');
            bw.write(ls.get(2));
            bw.write(',');
            bw.write(ls.get(3));
            bw.write(',');
            bw.write(ls.get(4));
            bw.write(',');
            bw.write(ls.get(5));
            bw.write(',');
            bw.write(ls.get(6));
            bw.newLine();
            bw.flush();
            bw.close();
            fw.close();
        }
    }

    public static void main(String[] args) throws Exception {
        SchedulerFactory schedFact = new StdSchedulerFactory();
        Scheduler sched = schedFact.getScheduler();
        sched.start();
        Calendar meiaNoite = Calendar.getInstance();
        meiaNoite.set(Calendar.HOUR_OF_DAY, 0);
        meiaNoite.set(Calendar.MINUTE, 0);
        meiaNoite.set(Calendar.SECOND, 0);
        meiaNoite.set(Calendar.MILLISECOND, 0);
        meiaNoite.add(Calendar.DATE, 1);
        JobDetail job = JobBuilder.newJob(ImportJSON.class).withIdentity("job1", "group1").build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(meiaNoite.getTime())
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForTotalCount(1440)).build();
        sched.scheduleJob(job, trigger);
    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            importa();
        } catch (Exception e) {
            e.printStackTrace();
            throw new JobExecutionException(e);
        }
    }
}
