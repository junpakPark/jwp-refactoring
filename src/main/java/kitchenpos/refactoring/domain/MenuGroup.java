package kitchenpos.refactoring.domain;

import org.springframework.data.annotation.Id;

public class MenuGroup {
    @Id
    private Long id;
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(String name) {
        this(null, name);
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}