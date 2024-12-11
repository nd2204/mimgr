package dev.mimgr.db;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryBuilder {
  private String table;
  private List<String> fields = new ArrayList<>();
  private List<String> conditions = new ArrayList<>();
  private List<String> groupby = new ArrayList<>();
  private List<String> joins = new ArrayList<>();
  private List<Object> parameters = new ArrayList<>();

  public QueryBuilder from(String table) {
    this.table = table;
    return this;
  }

  public QueryBuilder select(String... fields) {
    for (String field : fields) {
      this.fields.add(field);
    }
    return this;
  }

  public QueryBuilder where(String... conditions) {
    for (String cond : conditions) {
      this.conditions.add(cond);
    }
    return this;
  }

  public QueryBuilder join(String... joins) {
    for (String join : joins) {
      this.joins.add(join);
    }
    return this;
  }

  public QueryBuilder groupby(String... gb) {
    for (String g : gb) {
      this.groupby.add(g);
    }
    return this;
  }

  public List<Object> getParameters() {
    return this.parameters;
  }

  public String build() {
    StringBuilder query = new StringBuilder("SELECT ");

    if (fields.isEmpty()) {
      query.append("*");
    } else {
      query.append(String.join(", ", this.fields));
    }

    query.append(" FROM ").append(this.table);

    if (!joins.isEmpty()) {
      query.append(" ").append(String.join(" ", this.joins));
    }

    if (!conditions.isEmpty()) {
      query.append(" WHERE ").append(String.join(" AND ", this.conditions));
    }

    if (!groupby.isEmpty()) {
      query.append(" GROUP BY ").append(String.join(",", this.groupby));
    }

    return query.toString();
  }

  public static String buildList(List<?> objs) {
    StringBuilder query = new StringBuilder("(");
    query.append(String.join(
      ",",
      objs
        .stream()
        .map(String::valueOf)
        .collect(Collectors.toList())
    ));
    query.append(")");
    return query.toString();
  }
}
