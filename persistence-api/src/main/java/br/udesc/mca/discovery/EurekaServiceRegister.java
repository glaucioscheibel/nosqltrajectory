package br.udesc.mca.discovery;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;

public class EurekaServiceRegister implements Runnable{
    public static final String STARTING = "STARTING";
    public static final String UP = "UP";
    public static final String DOWN = "DOWN";

    private String vipAddress = "trajectory.udesc.br";
    private String eurekaServiceURL;
    private String appName;
    private int appPort;
    private String appServiceURI;
    private String healthcheckURI = "/healthcheck";
    private String statusURI = "/status";
    private String status;
    private String instanceId;
    private String appHost;
    private String appIp;
    private String protocol = "http";

    public EurekaServiceRegister(String appName, int appPort, String appServiceURI){
        this.appName = appName;
        this.appPort = appPort;
        this.appServiceURI = appServiceURI;

        try {
            appHost =InetAddress.getLocalHost().getHostName();
            appIp = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void register(String eurekaServiceURL) {
        this.eurekaServiceURL = eurekaServiceURL;
        this.status = STARTING;
        Thread t = new Thread(this);
        t.start();
    }

    public void serviceUp() {
        this.status = UP;
    }

    public void unregister() {
        this.status = DOWN;
    }

    public String getEurekaServiceURL() {
        return eurekaServiceURL;
    }

    public void setEurekaServiceURL(String eurekaServiceURL) {
        this.eurekaServiceURL = eurekaServiceURL;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getAppPort() {
        return appPort;
    }

    public void setAppPort(int appPort) {
        this.appPort = appPort;
    }

    public String getAppServiceURI() {
        return appServiceURI;
    }

    public void setAppServiceURI(String appServiceURI) {
        this.appServiceURI = appServiceURI;
    }

    public String getHealthcheckURI() {
        return healthcheckURI;
    }

    public void setHealthcheckURI(String healthcheckURI) {
        this.healthcheckURI = healthcheckURI;
    }

    public String getStatusURI() {
        return statusURI;
    }

    public void setStatusURI(String statusURI) {
        this.statusURI = statusURI;
    }

    public String getAppHost(){
        return this.appHost;
    }
    public void setAppHost(String host){
        this.appHost = host;
    }

    public String getProtocol() {return protocol;}
    public void setProtocol(String protocol) {this.protocol = protocol;}

    @Override
    public void run() {
        Proxy proxy;

        if(! "".equals(System.getProperty("http.proxyHost", ""))){
            SocketAddress address = new InetSocketAddress(System.getProperty("http.proxyHost"),
                    Integer.parseInt(System.getProperty("http.proxyPort")));
            proxy = new Proxy(Proxy.Type.HTTP, address);
        } else {
            proxy = Proxy.NO_PROXY;
        }


        while(true) {
            try {
                if (STARTING.equals(status)) {
                    instanceId = appHost;
                    URL url = new URL(eurekaServiceURL + "/apps/" + appName);
                    int response = -1;
                    do  {
                        try {
                            HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
                            con.setRequestMethod("POST");
                            con.setDoInput(true);
                            con.setDoOutput(true);
                            con.setRequestProperty("Content-Type", "application/json");

                            OutputStream out = con.getOutputStream();
                            out.write(createRegisterMessage().getBytes());
                            out.flush();
                            out.close();
                            response = con.getResponseCode();

                            System.out.println("Register eureka: " + response);

                            con.disconnect();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e){
                            status = DOWN;
                            break;
                        }
                    } while(response != 204);
                    status = UP;
                } else if (UP.equals(status)) {
                    try {
                        URL url = new URL(eurekaServiceURL + "/apps/" + appName + "/" + instanceId);

                        HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
                        con.setRequestMethod("PUT");
                        con.setDoInput(true);
                        con.setDoOutput(false);
                        con.setRequestProperty("Content-Type", "application/json");

                        System.out.println("Heartbeat: " + con.getResponseCode());

                        con.disconnect();
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                } else if (DOWN.equals(status)) {
                    try {
                        URL url = new URL(eurekaServiceURL + "/apps/" + appName + "/" + instanceId);

                        HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
                        con.setRequestMethod("DELETE");
                        con.setDoInput(true);
                        con.setDoOutput(false);
                        con.setRequestProperty("Content-Type", "application/json");

                        System.out.println("Deregister eureka: " + con.getResponseCode());

                        con.disconnect();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e){
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private String createRegisterMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("\"instance\":")
                .append("{")
                .append("\"hostName\":").append("\"").append(appHost).append("\",")
                .append("\"app\":").append("\"").append(appName).append("\",")
                .append("\"ipAddr\":").append("\"").append(appIp).append("\",")
                .append("\"vipAddress\":").append("\"").append(vipAddress).append("\",")
                .append("\"secureVipAddress\":").append("\"").append(vipAddress).append("\",")
                .append("\"status\":").append("\"").append(UP).append("\",")
                .append("\"port\":{").append("\"$\":").append(appPort).append(",\"@enabled\":\"true\"},")
                .append("\"securePort\":{").append("\"$\":").append(443).append(",\"@enabled\":\"false\"},")
                .append("\"homePageUrl\":").append("\"").append(protocol).append("://").append(appHost).append(":").append(appPort).append(appServiceURI).append("\",")
                .append("\"statusPageUrl\":").append("\"").append(protocol).append("://").append(appHost).append(":").append(appPort).append(statusURI).append("\",")
                .append("\"healthCheckUrl\":").append("\"").append(protocol).append("://").append(appHost).append(":").append(appPort).append(healthcheckURI).append("\",")
                .append("\"dataCenterInfo\":")
                .append("{")
                .append("\"name\":\"MyOwn\"")
                .append("}")
                .append("}")
        .append("}");

        return sb.toString();
    }
}
