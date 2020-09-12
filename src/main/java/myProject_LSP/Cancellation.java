package myProject_LSP;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Table(name="Cancellation_table")
public class Cancellation {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer customerId;
    private String status;
    private Long orderId;

    @PrePersist
    public void onPrePersist(){
        CookCancelled cookCancelled = new CookCancelled();
        BeanUtils.copyProperties(this, cookCancelled);
        this.setStatus("COOK_CANCELLED");
        cookCancelled.setStatus("COOK_CANCELLED");
        cookCancelled.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        myProject_LSP.external.Delivery delivery = new myProject_LSP.external.Delivery();
        // mappings goes here
        delivery.setOrderId(this.getOrderId());
        delivery.setStatus("SHIP_CANCEL");
        CookApplication.applicationContext.getBean(myProject_LSP.external.DeliveryService.class)
            .shipCancel(delivery);


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }




}
