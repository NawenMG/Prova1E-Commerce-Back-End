package com.prova.e_commerce.dbCol.parametri;

import java.util.Optional;

public class ParamQueryCassandra {

    private boolean distinct = false; // Aggiunto per il supporto a DISTINCT
    private boolean all = false; // Aggiunto per il supporto a ALL
    private Optional<String> condizioneWhere = Optional.empty();
    private Optional<String> valoriWhere = Optional.empty();
    private Optional<String> like = Optional.empty();
    private Optional<String> in = Optional.empty();
    private Optional<String[]> between = Optional.empty();
    private boolean orderBy = false;
    private String orderByColumn = "";
    private boolean allowFiltering = false; // Cassandra-specific

    // Costruttore di default
    public ParamQueryCassandra() {}

    // Getters e Setters
    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
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

    public boolean isOrderBy() {
        return orderBy;
    }

    public void setOrderBy(boolean orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public boolean isAllowFiltering() {
        return allowFiltering;
    }

    public void setAllowFiltering(boolean allowFiltering) {
        this.allowFiltering = allowFiltering;
    }

    // Metodo per costruire la clausola SELECT
    public String buildSelectClause() {
        StringBuilder selectClause = new StringBuilder("SELECT ");

        if (distinct) {
            selectClause.append("DISTINCT ");
        }

        if (all) {
            selectClause.append("* ");
        } else {
            selectClause.append("column1, column2 "); // Modificare con le colonne specifiche necessarie
        }

        return selectClause.toString();
    }

    // Metodo per costruire la clausola WHERE
    public String buildWhereClause() {
        StringBuilder whereClause = new StringBuilder();

        condizioneWhere.ifPresent(condition -> whereClause.append("WHERE ").append(condition).append(" "));
        like.ifPresent(likeCondition -> whereClause.append("AND column LIKE '%").append(likeCondition).append("%' "));
        in.ifPresent(inValues -> whereClause.append("AND column IN (").append(inValues).append(") "));
        between.ifPresent(betweenValues -> whereClause.append("AND column BETWEEN ")
                .append(betweenValues[0]).append(" AND ").append(betweenValues[1]).append(" "));

        return whereClause.toString();
    }

    // Metodo per costruire l'ORDER BY
    public String buildOrderByClause() {
        return orderBy ? "ORDER BY " + orderByColumn + " " : "";
    }
}
