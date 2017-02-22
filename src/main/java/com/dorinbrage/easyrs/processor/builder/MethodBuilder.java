package com.dorinbrage.easyrs.processor.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

/**
 * The Method builder
 * 
 * @author Dorin Brage
 */
public class MethodBuilder {

  private final static String DEFAULT_TYPE = "void";
  static final String EMPTY_LINE = System.getProperty("line.separator");

  private String typeMethod;

  private String name;

  private Set<Modifier> modifiers;

  private List<String> statements;

  public MethodBuilder(Set<Modifier> modifiers) {
    super();
    this.modifiers = modifiers;
  }

  public MethodBuilder(String name) {
    super();
    this.typeMethod = DEFAULT_TYPE;
    this.name = name;
    modifiers = getCustomModifier(Modifier.PUBLIC);
    statements = new ArrayList<>();
  }

  public MethodBuilder(String typeMethod, String name) {
    super();
    this.typeMethod = typeMethod;
    this.name = name;
    modifiers = getCustomModifier(Modifier.PUBLIC);
  }

  public Set<Modifier> getModifiers() {
    return modifiers;
  }

  public void setModifiers(Set<Modifier> modifiers) {
    this.modifiers = modifiers;
  }

  public String getTypeMethod() {
    return typeMethod;
  }

  public void setTypeMethod(String typeMethod) {
    this.typeMethod = typeMethod;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getStatements() {
    return statements;
  }

  public void setStatements(List<String> statements) {
    this.statements = statements;
  }

  public MethodBuilder addStatements(String format, Object... args) {
    statements.add(String.format(format, args).toString());
    return this;
  }
  
  public MethodBuilder addEmptyLine(){
    statements.add(EMPTY_LINE);
    return this;
  }

  private Set<Modifier> getCustomModifier(Object... objects) {
    Set<Modifier> type = new HashSet<>();

    for (Object modifier : objects) {
      type.add((Modifier) modifier);
    }
    return type;
  }

  public static final String getEmptyLine() {
    return EMPTY_LINE;
  }

}
