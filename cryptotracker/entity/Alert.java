package com.task.cryptotracker.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.io.Serializable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alerts")
public class Alert implements Serializable {

    @Id
    @GeneratedValue
    private int alertid;
    private String currency;
    private float target_price;
    private Date createdat = new Date(System.currentTimeMillis());
    private String status = "NEW";
    private String username;
    public void setStatus(String status) {
        this.status = status;
    }
}
