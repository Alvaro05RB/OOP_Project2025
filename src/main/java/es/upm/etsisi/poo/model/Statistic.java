package es.upm.etsisi.poo.model;

import javax.persistence.*;

@Entity
@Table(name = "statistics")
public class Statistic {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "categoryType")
    private CategoryType category;

    @Column(name = "score")
    private Double score;

    @ManyToOne(optional = false, cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "statistics")
    private Player player;

    public Statistic() {
        // Necesario para Hibernate
    }

    public Statistic(CategoryType category, Player player){
        this.category = category;
        this.player = player;
        this.score = 0.0;
    }

    public Long getId() {
        return id;
    }

    public CategoryType getCategory() {
        return category;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

}
