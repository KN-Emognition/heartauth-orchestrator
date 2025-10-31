package knemognition.heartauth.orchestrator.shared.app.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Jwt payload with ecgTest data.
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-25T08:37:27.755305314+02:00[Europe/Warsaw]", comments = "Generator version: 7.15.0")
public class EcgTestTokenClaims {

  @Valid
  private List<Float> testEcg = new ArrayList<>();

  public EcgTestTokenClaims() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EcgTestTokenClaims(List<Float> testEcg) {
    this.testEcg = testEcg;
  }

  public EcgTestTokenClaims testEcg(List<Float> testEcg) {
    this.testEcg = testEcg;
    return this;
  }

  public EcgTestTokenClaims addTestEcgItem(Float testEcgItem) {
    if (this.testEcg == null) {
      this.testEcg = new ArrayList<>();
    }
    this.testEcg.add(testEcgItem);
    return this;
  }

  /**
   * Get testEcg
   * @return testEcg
   */
  @NotNull 
  @JsonProperty("testEcg")
  public List<Float> getTestEcg() {
    return testEcg;
  }

  public void setTestEcg(List<Float> testEcg) {
    this.testEcg = testEcg;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EcgTestTokenClaims ecgTestTokenClaims = (EcgTestTokenClaims) o;
    return Objects.equals(this.testEcg, ecgTestTokenClaims.testEcg);
  }

  @Override
  public int hashCode() {
    return Objects.hash(testEcg);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EcgTestTokenClaims {\n");
    sb.append("    testEcg: ").append(toIndentedString(testEcg)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
  public static class Builder {

    private EcgTestTokenClaims instance;

    public Builder() {
      this(new EcgTestTokenClaims());
    }

    protected Builder(EcgTestTokenClaims instance) {
      this.instance = instance;
    }

    protected Builder copyOf(EcgTestTokenClaims value) { 
      this.instance.setTestEcg(value.testEcg);
      return this;
    }

    public Builder testEcg(List<Float> testEcg) {
      this.instance.testEcg(testEcg);
      return this;
    }
    
    /**
    * returns a built EcgTestTokenClaims instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public EcgTestTokenClaims build() {
      try {
        return this.instance;
      } finally {
        // ensure that this.instance is not reused
        this.instance = null;
      }
    }

    @Override
    public String toString() {
      return getClass() + "=(" + instance + ")";
    }
  }

  /**
  * Create a builder with no initialized field (except for the default values).
  */
  public static Builder builder() {
    return new Builder();
  }

  /**
  * Create a builder with a shallow copy of this instance.
  */
  public Builder toBuilder() {
    Builder builder = new Builder();
    return builder.copyOf(this);
  }

}

