package myProject_LSP;

import myProject_LSP.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }
    @Autowired
    CookRepository cookRepository;
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrdered_Accept(@Payload Ordered ordered){

        if(ordered.isMe()){

            System.out.println("##### listener Accept : " + ordered.toJson());

            Cook cook = new Cook();
            //cook.setOrderId(ordered.getId());
            //cook.setCustomerId(ordered.getCustomerId());
            BeanUtils.copyProperties(ordered, cook);
            cook.setOrderId(ordered.getId());
            cook.setStatus("COOKED");

            cookRepository.save(cook);
        }
    }

}
