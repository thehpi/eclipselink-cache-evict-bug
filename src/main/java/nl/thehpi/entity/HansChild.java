package nl.thehpi.entity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.UUID;

@Entity
public class HansChild {
    @Id
    private String id;

    @PrePersist
    private void prePersist() {
        if (id != null) {
            return;
        }
        id = UUID.randomUUID().toString();
    }

    private String test;

    @JsonbTransient
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Hans hans;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public void setHans(Hans hans) {
        this.hans = hans;
    }

    public Hans getHans() {
        return hans;
    }
}
