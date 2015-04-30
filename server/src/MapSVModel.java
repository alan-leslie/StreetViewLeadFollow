import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;


public class MapSVModel {
	static final private String mapCentreKey = "map_center"; 
	static final private String mapZoomKey = "map_zoom"; 
	static final private String svPosKey = "pano_pos"; 
	static final private String svZoomKey = "pano_zoom"; 
	static final private String svHeadingKey = "pano_heading"; 
	static final private String svPitchKey = "pano_pitch"; 

	private String mapCentre;
	private int mapZoom;
	private String svPos;
	private int svZoom;
	private float svHeading;
	private float svPitch;
	
	public MapSVModel(){	
	}
	
	public String getMapCentre() {
		return mapCentre;
	}
	public void setMapCentre(String mapCentreLat, String mapCentreLon) {
		this.mapCentre = mapCentreLat + "," + mapCentreLon;
	}
	public int getMapZoom() {
		return mapZoom;
	}
	public void setMapZoom(int mapZoom) {
		this.mapZoom = mapZoom;
	}
	public String getSvPos() {
		return svPos;
	}
	public void setSvPos(String svPosLat, String svPosLon) {
		this.svPos = svPosLat + "," + svPosLon;
	}
	public int getSvZoom() {
		return svZoom;
	}
	public void setSvZoom(int svZoom) {
		this.svZoom = svZoom;
	}
	public float getSvHeading() {
		return svHeading;
	}
	public void setSvHeading(float svHeading) {
		this.svHeading = svHeading;
	}
	public float getSvPitch() {
		return svPitch;
	}
	public void setSvPitch(float svPitch) {
		this.svPitch = svPitch;
	}
	
	public JSONObject asJSONObject(){
		Map<String, String> theMap = new HashMap<String, String>();
		
		theMap.put(mapCentreKey, getMapCentre());
		theMap.put(mapZoomKey, Integer.toString(getMapZoom()));
		theMap.put(svPosKey, getSvPos());
		theMap.put(svZoomKey, Integer.toString(getSvZoom()));
		theMap.put(svHeadingKey, Float.toString(getSvHeading()));
		theMap.put(svPitchKey, Float.toString(getSvPitch()));
		
		JSONObject theObject = new JSONObject(theMap);
		
		return theObject;
	}
	
	public boolean updateModel(String message){
		boolean retVal = true;
		String type = getMessageType(message);
		String details = getMessageDetails(message);
        String[] detailsTokens = details.replace("(", "").replace(")", "").split(",");
				
		switch(type){
		    case "map_center":
			// reset map centre
			String lat = detailsTokens[0];
			String lon = detailsTokens[1];
			setMapCentre(lat,lon); 
			break;
		case "map_zoom":
			// reset map zoom - parseInt
			int zoom = Integer.valueOf(details);
			setMapZoom(zoom); 
			break;
		case "pano_pos":
			// reset pano pos
			String svLat = detailsTokens[0];
			String svLon = detailsTokens[1];
			setSvPos(svLat, svLon);
			break;
		case "pano_pov":
			// reset pano pov (heading and pitch);
			float heading = Float.valueOf(detailsTokens[0]);
			float pitch = Float.valueOf(detailsTokens[1]);

			setSvHeading(heading);
			setSvPitch(pitch);
			break;
		case "pano_zoom":
			// doesn't seem to be an event for this
			// reset map zoom - parseInt
			int svZoom = Integer.valueOf(details);
			setSvZoom(svZoom); 
			break;
		default:
			retVal = false;
	}
		
		return retVal;
	}
	
	public String getMessageType(String message){
	    String messageType = "message";
		int fieldSeparatorIndex = message.indexOf(':');
		
		if(fieldSeparatorIndex != -1){
			messageType = message.substring(0, fieldSeparatorIndex);
		}
		
		if(messageType == "lead" || messageType == "follow"){
			messageType = "message";
		}
			
		return messageType;
	}

	private String getMessageDetails(String message){
	    String messageType = "message";
	    String messageDetails = new String(message);
		int fieldSeparatorIndex = message.indexOf(':');
		
		if(fieldSeparatorIndex != -1){
			messageType = message.substring(0, fieldSeparatorIndex);
			messageDetails = message.substring(fieldSeparatorIndex + 1);
		}
		
		if(messageType == "lead" || messageType == "follow"){
			messageType = "message";
			messageDetails = message;
		}
			
		return messageDetails;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mapCentre == null) ? 0 : mapCentre.hashCode());
		result = prime * result + mapZoom;
		result = prime * result + (int)(svHeading * 1000);
		result = prime * result + (int)(svPitch * 1000);
		result = prime * result + ((svPos == null) ? 0 : svPos.hashCode());
		result = prime * result + svZoom;
		result = prime * result
				+ ((svZoomKey == null) ? 0 : svZoomKey.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapSVModel other = (MapSVModel) obj;
		if (mapCentre == null) {
			if (other.mapCentre != null)
				return false;
		} else if (!mapCentre.equals(other.mapCentre))
			return false;
		if (mapZoom != other.mapZoom)
			return false;
		if (svHeading != other.svHeading)
			return false;
		if (svPitch != other.svPitch)
			return false;
		if (svPos == null) {
			if (other.svPos != null)
				return false;
		} else if (!svPos.equals(other.svPos))
			return false;
		if (svZoom != other.svZoom)
			return false;
		return true;
	}
}
