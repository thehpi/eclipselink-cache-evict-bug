package nl.thehpi.entity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Cacheable(false)
public class HansFriend {
    @Id
    private String id;

    @PrePersist
    private void prePersist() {
        if (id != null) {
            return;
        }
        id = UUID.randomUUID().toString();
    }

    @JsonbTransient
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Hans hans;

    private String test;

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

    public Hans getHans() {
        return hans;
    }

    public void setHans(Hans hans) {
        this.hans = hans;
    }
}
