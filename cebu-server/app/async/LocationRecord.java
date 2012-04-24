package async;

import gov.sandia.cognition.math.matrix.Vector;
import gov.sandia.cognition.math.matrix.VectorFactory;

import inference.InferenceService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.operation.TransformException;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Coordinate;

public class LocationRecord {

  private final String vehicleId;
  private final Date timestamp;
  private final Coordinate obsCoords;
  private final Coordinate obsPoint;
  private final Vector projPoint;

  private final Double velocity;

  private final Double heading;
  private final Double accuracy;
  private final LocationRecord prevLoc;

  /*
   * This map is how we keep track of the previous records for each vehicle.
   * TODO might want to null the previous record member at some point.
   */
  private static Map<String, LocationRecord> vehiclesToRecords = Maps
      .newConcurrentMap();
  
  private static final SimpleDateFormat sdf = new SimpleDateFormat(
      "yyyy-MM-dd hh:mm:ss");

  private LocationRecord(String vehicleId, Date timestamp, Coordinate obsCoords,
    Coordinate obsPoint, Double velocity, Double heading, Double accuracy, 
    LocationRecord prevLoc) {
    super();
    this.vehicleId = vehicleId;
    this.timestamp = timestamp;
    this.obsCoords = obsCoords;
    this.obsPoint = obsPoint;
    this.velocity = velocity;
    this.heading = heading;
    this.accuracy = accuracy;
    this.projPoint = VectorFactory.getDefault().createVector2D(obsPoint.x,
        obsPoint.y);
    this.prevLoc = prevLoc;
  }

  public static LocationRecord createLocationRecord(String vehicleId,
    String timestamp, String latStr, String lonStr, String velocity,
    String heading, String accuracy) throws NumberFormatException,
      ParseException, TransformException {
    final double lat = Double.parseDouble(latStr);
    final double lon = Double.parseDouble(lonStr);
    final Coordinate obsCoords = new Coordinate(lon, lat);
    final Coordinate obsPoint = new Coordinate();

    JTS.transform(obsCoords, obsPoint, InferenceService.getTransform());

    final LocationRecord prevLocation = vehiclesToRecords.get(vehicleId);

    final LocationRecord location = new LocationRecord(vehicleId,
        sdf.parse(timestamp), obsCoords, obsPoint,
        velocity != null ? Double.parseDouble(velocity) : null,
        heading != null ? Double.parseDouble(heading) : null,
        accuracy != null ? Double.parseDouble(accuracy) : null, prevLocation);

    vehiclesToRecords.put(location.getVehicleId(), location);
    
    return location;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    LocationRecord other = (LocationRecord) obj;
    if (accuracy == null) {
      if (other.accuracy != null) {
        return false;
      }
    } else if (!accuracy.equals(other.accuracy)) {
      return false;
    }
    if (heading == null) {
      if (other.heading != null) {
        return false;
      }
    } else if (!heading.equals(other.heading)) {
      return false;
    }
    if (obsCoords == null) {
      if (other.obsCoords != null) {
        return false;
      }
    } else if (!obsCoords.equals(other.obsCoords)) {
      return false;
    }
    if (obsPoint == null) {
      if (other.obsPoint != null) {
        return false;
      }
    } else if (!obsPoint.equals(other.obsPoint)) {
      return false;
    }
    if (projPoint == null) {
      if (other.projPoint != null) {
        return false;
      }
    } else if (!projPoint.equals(other.projPoint)) {
      return false;
    }
    if (timestamp == null) {
      if (other.timestamp != null) {
        return false;
      }
    } else if (!timestamp.equals(other.timestamp)) {
      return false;
    }
    if (vehicleId == null) {
      if (other.vehicleId != null) {
        return false;
      }
    } else if (!vehicleId.equals(other.vehicleId)) {
      return false;
    }
    if (velocity == null) {
      if (other.velocity != null) {
        return false;
      }
    } else if (!velocity.equals(other.velocity)) {
      return false;
    }
    return true;
  }

  public Double getAccuracy() {
    return accuracy;
  }

  public Double getHeading() {
    return heading;
  }

  public Coordinate getObsCoords() {
    return obsCoords;
  }

  public Coordinate getObsPoint() {
    return obsPoint;
  }

  public Vector getProjPoint() {
    return projPoint;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getVehicleId() {
    return vehicleId;
  }

  public Double getVelocity() {
    return velocity;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((accuracy == null) ? 0 : accuracy.hashCode());
    result = prime * result + ((heading == null) ? 0 : heading.hashCode());
    result = prime * result + ((obsCoords == null) ? 0 : obsCoords.hashCode());
    result = prime * result + ((obsPoint == null) ? 0 : obsPoint.hashCode());
    result = prime * result + ((projPoint == null) ? 0 : projPoint.hashCode());
    result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
    result = prime * result + ((vehicleId == null) ? 0 : vehicleId.hashCode());
    result = prime * result + ((velocity == null) ? 0 : velocity.hashCode());
    return result;
  }

  public LocationRecord getPrevLoc() {
    return prevLoc;
  }

}