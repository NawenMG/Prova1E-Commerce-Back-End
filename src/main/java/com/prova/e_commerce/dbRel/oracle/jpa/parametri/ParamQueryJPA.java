package com.prova.e_commerce.dbRel.oracle.jpa.parametri;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ParamQueryJPA {

    // Variabili di configurazione per le query
    private boolean distinct = false;
    private boolean all = false;
    private Optional<String> condizioneWhere = Optional.empty();
    private Optional<String> valoriWhere = Optional.empty();
    private Optional<String> boleani = Optional.empty();
    private boolean orderBy = false;
    private Integer top = null; // Utilizzare Integer per supportare il valore null
    private AggregationType aggregationType = AggregationType.NONE; // Modificato con enum
    private Optional<String> like = Optional.empty();
    private Optional<String> in = Optional.empty();
    private Optional<String[]> between = Optional.empty();
    private Optional<String> having = Optional.empty();
    private int limit = 40; // Limite per paginazione
    private int offset = limit; // Offset per paginazione

    // Enum per rappresentare i vari tipi di aggregazione
    public enum AggregationType {
        NONE, MIN, MAX, COUNT, AVG, SUM
    }

    // Costruttore di default
    public ParamQueryJPA() {
        // I valori di default sono già stati impostati
    }

    // Getters e Setters
    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public Optional<String> getCondizioneWhere() {
        return condizioneWhere;
    }

    public void setCondizioneWhere(Optional<String> condizioneWhere) {
        this.condizioneWhere = condizioneWhere;
    }

    public Optional<String> getValoriWhere() {
        return valoriWhere;
    }

    public void setValoriWhere(Optional<String> valoriWhere) {
        this.valoriWhere = valoriWhere;
    }

    public Optional<String> getBoleani() {
        return boleani;
    }

    public void setBoleani(Optional<String> boleani) {
        this.boleani = boleani;
    }

    public boolean isOrderBy() {
        return orderBy;
    }

    public void setOrderBy(boolean orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public AggregationType getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(AggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }

    public Optional<String> getLike() {
        return like;
    }

    public void setLike(Optional<String> like) {
        this.like = like;
    }

    public Optional<String> getIn() {
        return in;
    }

    public void setIn(Optional<String> in) {
        this.in = in;
    }

    public Optional<String[]> getBetween() {
        return between;
    }

    public void setBetween(Optional<String[]> between) {
        this.between = between;
    }

    public Optional<String> getHaving() {
        return having;
    }

    public void setHaving(Optional<String> having) {
        this.having = having;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        // Verifica che il limite sia positivo
        this.limit = (limit > 0) ? limit : 40;  // valore predefinito
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean getAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    // Metodo per costruire la clausola WHERE utilizzando Criteria API
    public CriteriaQuery<?> buildWhereClause(EntityManager em, Class<?> entityClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<?> cq = cb.createQuery(entityClass);
        Root<?> root = cq.from(entityClass);
        
        List<Predicate> predicates = new ArrayList<>();

        // Condizione WHERE principale
        condizioneWhere.ifPresent(condition -> predicates.add(cb.equal(root.get("column"), condition)));

        // Condizione LIKE
        like.ifPresent(likeCondition -> predicates.add(cb.like(root.get("column"), "%" + likeCondition + "%")));

       // Condizione IN
       in.ifPresent(inValues -> {
         String[] valuesArray = inValues.split(",");  // Assumiamo che inValues sia una stringa separata da virgole
         predicates.add(root.get("column").in((Object[]) valuesArray));  // Cast dell'array in un Object[]
       });
        // Condizione BETWEEN
        between.ifPresent(betweenValues -> predicates.add(cb.between(root.get("column"), betweenValues[0], betweenValues[1])));

        // Aggiungi le predicati
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return cq;
    }

    // Metodo per costruire la parte di aggregazione (es. MIN, MAX, COUNT, AVG, SUM)
    public String buildAggregationClause() {
        switch (aggregationType) {
            case MIN:
                return "MIN(column)";
            case MAX:
                return "MAX(column)";
            case COUNT:
                return "COUNT(column)";
            case AVG:
                return "AVG(column)";
            case SUM:
                return "SUM(column)";
            case NONE:
            default:
                return "column";
        }
    }

    // Metodo per costruire la parte ORDER BY della query
    public String buildOrderByClause() {
        return orderBy ? "ORDER BY column" : "";
    }

    // Metodo per gestire il TOP
    public String buildTopClause() {
        return top != null ? "FETCH FIRST " + top + " ROWS ONLY" : "";
    }
}