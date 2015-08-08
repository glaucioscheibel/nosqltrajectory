<%@ page language="java" import="java.util.*,java.util.Map.Entry,com.netflix.discovery.shared.Pair,com.netflix.discovery.shared.*, com.netflix.eureka.util.*,com.netflix.appinfo.InstanceInfo.*, com.netflix.appinfo.DataCenterInfo.*,com.netflix.appinfo.AmazonInfo.MetaDataKey,com.netflix.eureka.resources.*,com.netflix.eureka.*,com.netflix.appinfo.*" pageEncoding="UTF-8" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
  <head>
    <base href="<%=basePath%>">
        <div>
           <%
           StringBuilder sb = new StringBuilder("<br><br><table>");
           sb.append("<tr><th>Service</th>");
           sb.append("<th>Host</th>");
           sb.append("<th>Port</th>");
           sb.append("<th>Status</th>");
           sb.append("<th>ServiceUrl</th></tr>");

            for(Application app : PeerAwareInstanceRegistryImpl.getInstance().getSortedApplications()) {
                Map<String, Integer> amiCounts = new HashMap<String, Integer>();
                Map<InstanceInfo.InstanceStatus,List<Pair<String, String>>> instancesByStatus =
                        new HashMap<InstanceInfo.InstanceStatus, List<Pair<String,String>>>();
                Map<String,Integer> zoneCounts = new HashMap<String, Integer>();


                for(InstanceInfo info : app.getInstances()){
                String url = info.getHomePageUrl();
                if("EUREKA".equals(info.getAppName())){
                    url = "http://" + info.getHostName() + ":" + info.getPort() +"/persistence-discovery/jsp/services.jsp";
                }

                    sb.append("<tr>");
                    sb.append("<td>").append(info.getAppName()).append("</td>");
                    sb.append("<td>").append(info.getHostName()).append("</td>");
                    sb.append("<td>").append(info.getPort()).append("</td>");
                    sb.append("<td>").append(info.getStatus()).append("</td>");
                    sb.append("<td><a href='").append(url).append("'>").append(url).append("</a></td>");
                    sb.append("</tr>");

                    String id = info.getId();
                    String statusUrl = info.getStatusPageUrl();
                }
            }
                sb.append("</table>");
                out.println(sb.toString());
           %>
      </div>
  </body>
</html>
