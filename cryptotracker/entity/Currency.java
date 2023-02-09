package com.task.cryptotracker.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import com.task.cryptotracker.entity.Alert;
import java.util.List;
import javax.persistence.*;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "currencies")
public class Currency implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private float price;
    private int enabled;
    private String symbol;
    private Date creationTime = new Date(System.currentTimeMillis());
    @OneToMany(targetEntity = Alert.class, cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "currency", referencedColumnName = "name")
    private List<Alert> alerts;
}
