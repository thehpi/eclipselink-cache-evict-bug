package nl.thehpi.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
public class Hans {

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

    @OneToMany(mappedBy = "hans", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Collection<HansChild> children = new ArrayList<>();

    @OneToMany(mappedBy = "hans", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Collection<HansFriend> friends;

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

    public Collection<HansFriend> getFriends() {
        return friends;
    }

    public void addFriend(HansFriend friend) {
        this.friends.add(friend);
        friend.setHans(this);
    }

    public Collection<HansChild> getChildren() {
        return children;
    }

    public void addChild(HansChild hansChild) {
        this.children.add(hansChild);
        hansChild.setHans(this);
    }

    public void removeChild(HansChild hansChild) {
        this.children.remove(hansChild);
        hansChild.setHans(null);
    }

    public void setChildren(Collection<HansChild> children) {
        this.children = children;
    }

    public HansChild getChild(String childId) {
        if (children == null) {
            return null;
        }
        for (HansChild child : children) {
            if (child.getId().equals(childId)) {
                return child;
            }
        }
        return null;
    }
}
