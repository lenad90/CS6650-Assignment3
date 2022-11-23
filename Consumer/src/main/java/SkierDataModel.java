import io.swagger.client.model.LiftRide;

public final class SkierDataModel {
  private Object resort;
  private Object season;
  private Object day;
  private Object time;
  private Object verticalDay;
  private Object liftID;

  public SkierDataModel(Object resortID, Object seasonID, Object dayID,
      Object time, Object liftID) {
    this.resort = resortID;
    this.season = seasonID;
    this.day = dayID;
    this.time = time;
    this.liftID = liftID;
    this.verticalDay = (int) liftID * 10;
  }

  public Object getResortID() {
    return resort;
  }

  public Object getSeasonID() {
    return season;
  }

  public Object getDayID() {
    return day;
  }

  public Object getTime() {
    return time;
  }

  public Object getLiftID() {
    return liftID;
  }

  @Override
  public String toString() {
    return "{" +
        "resort:" + resort +
        ", season:" + season +
        ", day:" + day +
        ", time:" + time +
        ", verticalDay:" + verticalDay +
        ", liftID:" + liftID +
        '}';
  }
}
