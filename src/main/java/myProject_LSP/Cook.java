package myProject_LSP;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Cook_table")
public class Cook {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer customerId;
    private String status;
    private Long orderId;

    //추가부분
    private static Integer qty = 2;
    private boolean cookChk;

    @PostPersist
    public void onPostPersist(){
        System.out.println("@@@@@@@@@@@@@24");

        Cooked cooked = new Cooked();
        BeanUtils.copyProperties(this, cooked);
        //추가부분

        System.out.println("@@@@@@@@@@@@@@@@@@cookChk : " + cookChk);
        if(cookChk){
            cooked.publishAfterCommit();
        }

        //if(!cookChk){
        //    cooked.setStatus("QTY_COOK_CANCELLED");
        // }
    }

    @PrePersist
    public void onPrePersist(){


        if((qty-1)<0){
            //재고량이 없을 경우, 요리취소
            cookChk = false;
            this.setStatus("QTY_COOK_CANCELLED");

            CookQtyChecked cookQtyChecked = new CookQtyChecked();
            cookQtyChecked.setStatus("QTY_COOK_CANCELLED");
            BeanUtils.copyProperties(this, cookQtyChecked);


            System.out.println(this.getCustomerId());
            System.out.println(cookQtyChecked.getCustomerId());
            //추가부분
            cookQtyChecked.publishAfterCommit();


        }else{
            //재고량이 있을 경우 요리진행
            cookChk = true;
            qty--;
        }

        System.out.println("@@@@@@@@@@@@qty : " + qty);



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
