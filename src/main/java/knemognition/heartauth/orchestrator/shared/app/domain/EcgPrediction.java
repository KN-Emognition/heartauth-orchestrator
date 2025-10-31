package knemognition.heartauth.orchestrator.shared.app.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * EcgPrediction
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-25T08:37:27.755305314+02:00[Europe/Warsaw]", comments = "Generator version: 7.15.0")
public class EcgPrediction {

  private Boolean prediction;

  private @Nullable Float score;

  private @Nullable String error;

  public EcgPrediction() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EcgPrediction(Boolean prediction) {
    this.prediction = prediction;
  }

  public EcgPrediction prediction(Boolean prediction) {
    this.prediction = prediction;
    return this;
  }

  /**
   * Get prediction
   * @return prediction
   */
  @NotNull 
  @JsonProperty("prediction")
  public Boolean getPrediction() {
    return prediction;
  }

  public void setPrediction(Boolean prediction) {
    this.prediction = prediction;
  }

  public EcgPrediction score(@Nullable Float score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  
  @JsonProperty("score")
  public @Nullable Float getScore() {
    return score;
  }

  public void setScore(@Nullable Float score) {
    this.score = score;
  }

  public EcgPrediction error(@Nullable String error) {
    this.error = error;
    return this;
  }

  /**
   * Get error
   * @return error
   */
  
  @JsonProperty("error")
  public @Nullable String getError() {
    return error;
  }

  public void setError(@Nullable String error) {
    this.error = error;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EcgPrediction ecgPrediction = (EcgPrediction) o;
    return Objects.equals(this.prediction, ecgPrediction.prediction) &&
        Objects.equals(this.score, ecgPrediction.score) &&
        Objects.equals(this.error, ecgPrediction.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prediction, score, error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EcgPrediction {\n");
    sb.append("    prediction: ").append(toIndentedString(prediction)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
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

    private EcgPrediction instance;

    public Builder() {
      this(new EcgPrediction());
    }

    protected Builder(EcgPrediction instance) {
      this.instance = instance;
    }

    protected Builder copyOf(EcgPrediction value) { 
      this.instance.setPrediction(value.prediction);
      this.instance.setScore(value.score);
      this.instance.setError(value.error);
      return this;
    }

    public Builder prediction(Boolean prediction) {
      this.instance.prediction(prediction);
      return this;
    }
    
    public Builder score(Float score) {
      this.instance.score(score);
      return this;
    }
    
    public Builder error(String error) {
      this.instance.error(error);
      return this;
    }
    
    /**
    * returns a built EcgPrediction instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public EcgPrediction build() {
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

