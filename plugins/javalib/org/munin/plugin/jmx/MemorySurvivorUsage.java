package org.munin.plugin.jmx;
import java.lang.management.ManagementFactory.*;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.FileNotFoundException;
import java.io.IOException;
public class MemorySurvivorUsage {

    public static void main(String args[]) throws FileNotFoundException, IOException {
        String[] connectionInfo = ConfReader.GetConnectionInfo();

        if (args.length == 1) {
            if (args[0].equals("config")) {
                System.out.println("graph_title JVM (port " + connectionInfo[1] + ") MemorySurvivorUsage\n" +
                        "graph_vlabel bytes\n" +
			"graph_category " + connectionInfo[2] + "\n" +
                        "graph_info Returns an estimate of the memory usage of this memory pool.\n" +
                        "Committed.label Committed\n" +
                        "Committed.info The amount of memory (in bytes) that is guaranteed to be available for use by the Java virtual machine.\n" +
                        "Max.label Max\n" +
                        "Max.info The maximum amount of memory (in bytes) that can be used for memory management.\n" +
                        "Max.draw AREA\n" +
                        "Max.colour ccff00\n" +
                        "Init.label Init\n" +
                        "Init.info The initial amount of memory (in bytes) that the Java virtual machine requests from the operating system for memory management during startup.\n" +
                        "Used.label Used\n" +
                        "Used.info represents the amount of memory currently used (in bytes).\n" +
                        "Threshold.label Threshold\n" +
                        "Threshold.info Returns the usage threshold value of this memory pool in bytes.\n"
                        );
            }
         else {
            try {

                JMXServiceURL u = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + connectionInfo[0] + ":" + connectionInfo[1]+ "/jmxrmi");
                JMXConnector c = JMXConnectorFactory.connect(u);
                MBeanServerConnection connection = c.getMBeanServerConnection();

                GetUsage collector = new GetUsage(connection, 4);
                String[] temp = collector.GC();

                System.out.println("Committed.value " + temp[0]);
                System.out.println("Init.value " + temp[1]);
                System.out.println("Max.value "+temp[2]);
                System.out.println("Used.value "+temp[3]);
                System.out.println("Threshold.value "+temp[4]);

            } catch (Exception e) {
                System.out.print(e);
            }
        }
    }
}
}
