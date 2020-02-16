package yellowcab;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import java.util.stream.Stream;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import static yellowcab.LocListener.locationID;
import static yellowcab.SecondTask.timeFrame;
import static yellowcab.SecondTask.tripFreq;

public class ThirdTask implements Runnable {

    synchronized public void run() {

        try {
            Integer maxVal = Collections.max(tripFreq); // should return 7
            Integer maxIdx = tripFreq.indexOf(maxVal);
            String fileName = "F:\\NUIG AI Semester 2\\Research Topics in AI\\Assignment 1\\taxi+_zone_lookup.csv";
            int date = SecondTask.date;
            int time = timeFrame.get(maxIdx);
            System.out.println("Peak time : " + time);
            System.out.println("Trip frequency on peak time : " + maxVal);
            String key = date +","+time;
            
            Collection<Integer> busiestLoc = locationID.get(key);
            
            Map<Integer, Long> collect = busiestLoc.stream().collect(groupingBy(Function.identity(), counting()));
            int maxLoc = collect.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
           
            try (Stream<String> data = Files.lines(Paths.get(fileName)).filter(s -> !s.isEmpty())) {
            data.skip(1).map(s -> new AbstractMap.SimpleEntry<>(s, s.split(",")))
                    .filter(s -> s.getValue()[0].equals(String.valueOf(maxLoc)))
                     .forEach(s -> { String locationDetails = s.getValue()[1]+","+s.getValue()[2];
                     System.out.println("Buisest Location : " +locationDetails);
                     });
//                     .filter(s -> s.getValue()[0].equals(maxLoc)).map(AbstractMap.SimpleEntry::getKey)
//            .collect(Collectors.toList());
            
            
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
