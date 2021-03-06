package org.azavea.otm.data;

import java.util.ArrayList;

import org.azavea.otm.App;
import org.azavea.otm.Field;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Tree extends Model {
	private Plot plot = null;
	
	public Tree() {
		data = new JSONObject();
	}

	public Tree(Plot parentPlot) {
		data = new JSONObject();
		plot = parentPlot;
	}
	
	public int getId() throws JSONException {
		return data.getInt("id");
	}

	public void setId(int id) throws JSONException {
		data.put("id", id);
	}

	public double getDbh() throws JSONException {
		return getDbh(false);
	}
	
	public double getDbh(boolean getCurrentOnly) throws JSONException {
		if (getCurrentOnly) {
			return getDoubleOrDefault("dbh", 0d);
		}
		
		// Use the field value, which could be a recent pending value
		Object value = Field.getValueForKey("tree.dbh", plot);
		if (value != null) {
			return Double.parseDouble(value.toString());
		} else {
			return 0d;
		}
		
	}

	public void setDbh(double dbh) throws JSONException {
		data.put("dbh", dbh);
	}
	
	public String getDateRemoved() throws JSONException {
		return data.getString("date_removed");
	}

	public void setDateRemoved(String dateRemoved) throws JSONException {
		data.put("date_removed", dateRemoved);
	}

	public String getSpeciesName() throws JSONException {
		return getSpeciesName(false);
	}
	
	/**
	 * Get the current or pending value for species name
	 * @param getCurrentOnly, if True return the actual saved value, otherwise return
	 * pending value if it exists
	 * @return
	 * @throws JSONException
	 */
	public String getSpeciesName(boolean getCurrentOnly) throws JSONException {
		if (getCurrentOnly) {
			return data.getString("species_name");
		}
		Object value = Field.getValueForKey("tree.species_name", plot);
		if (value != null) {
			return value.toString();
		} else {
			return null;
		}
	}

	public String getSpeciesName(String defaultText) throws JSONException {
		if (data.isNull("species_name")) {
			return defaultText;
		}
		return getSpeciesName();
	}
	
	public void setSpeciesName(String speciesName) throws JSONException {
		data.put("species_name", speciesName);
	}

	public String getTreeOwner() throws JSONException {
		return data.getString("tree_owner");
	}

	public void setTreeOwner(String treeOwner) throws JSONException {
		data.put("tree_owner", treeOwner);
	}

	public String getDatePlanted() throws JSONException {
		return data.getString("date_planted");
	}

	public void setDatePlanted(String datePlanted) throws JSONException {
		data.put("date_planted", datePlanted);
	}

	public String getCondition() throws JSONException {
		return data.getString("condition");
	}

	public void setCondition(String condition) throws JSONException {
		data.put("condition", condition);
	}

	public boolean isReadOnly() throws JSONException {
		// "readonly" can be null in which case getBoolean will barf.
		// I think the right thing to do for the case where it is null
		// (or unreadable bc of some other error) is to return false.
		try {
			return data.getBoolean("readonly");
		} catch (Exception e) {
			return false;
		}
	}

	public void setReadOnly(boolean readOnly) throws JSONException{
		data.put("readonly", readOnly);
	}

	public String getSpecies() throws JSONException{
		return data.getString("species");
	}

	public void setSpecies(String species) throws JSONException {
		data.put("species", species);
	}

	public String getSpeciesOther1() throws JSONException {
		return data.getString("species_other1");
	}

	public void setSpeciesOther1(String speciesOther1) throws JSONException {
		data.put("species_other1", speciesOther1);
	}

	public String getSpeciesOther2() throws JSONException {
		return data.getString("species_other2");
	}

	public void setSpeciesOther2(String speciesOther2) throws JSONException {
		data.put("species_other2", speciesOther2);
	}

	public String getLastUpdated() throws JSONException {
		return data.getString("last_updated");
	}

	public void setLastUpdated(String lastUpdated) throws JSONException {
		data.put("last_updated", lastUpdated);
	}

	public String getLastUpdatedBy() throws JSONException {
		return data.getString("last_updated_by");
	}

	public void setLastUpdatedBy(String lastUpdatedBy) throws JSONException {
		data.put("last_updated_by", lastUpdatedBy);
	}

	public boolean isPresent() throws JSONException {
		return data.getBoolean("present");
	}

	public void setPresent(boolean present) throws JSONException {
		data.put("present", present);
	}

	public String getSponsor() throws JSONException {
		return data.getString("sponsor");
	}

	public void setSponsor(String sponsor) throws JSONException {
		data.put("sponsor", sponsor);
	}

	public String getCanopyCondition() throws JSONException {
		return data.getString("canopy_condition");
	}

	public void setCanopyCondition(String canopyCondition) throws JSONException {
		data.put("canopy_condition", canopyCondition);
	}

	public String getScientificName() throws JSONException {
		return data.getString("sci_name");
	}

	public void setScientificName(String scientificName) throws JSONException {
		data.put("sci_name", scientificName);
	}

	public String getStewardName() throws JSONException {
		return data.getString("steward_name");
	}

	public void setStewardName(String stewardName) throws JSONException {
		data.put("steward_name", stewardName);
	}
	
	/**
	 * Add an image definition to the list of available images.  This is
	 * useful after a new image is added to the server, without having to
	 * request the plot/tree again
	 * @param image
	 * @throws JSONException
	 */
	public void addImageToList(JSONObject image) throws JSONException {
		JSONArray images;
		if (data.isNull("images")) {
			images = new JSONArray();
			data.put("images", images);
		} else {
			images = data.getJSONArray("images");	
		}
		images.put(image);
	}
	
	public ArrayList<Integer> getImageIdList() throws JSONException {	
		ArrayList<Integer> idList = new ArrayList<Integer>();
		if (data.isNull("images")) {
			return null;
		}
		JSONArray ids = data.getJSONArray("images");
		for (int i=0; i<ids.length(); i++) {
			JSONObject image = ids.getJSONObject(i);
			idList.add((Integer) image.getInt("id"));
		}
		return idList;
	}
	
	public String getTreePhotoUrl() throws JSONException {
		if (data.isNull("images")) {
			return null; 
		} 
		
		// loop through the image array and pluck out
		// the image with the highest ID (the most current image.)
		JSONArray images = data.getJSONArray("images");
		String urlLeaf = null;
		int maxId = -1;
		for (int i = 0; i< images.length(); i++) {
			JSONObject image = images.getJSONObject(i);
			int imageId = image.getInt("id");
			if (imageId > maxId) {
				maxId = imageId;
				urlLeaf = image.getString("url");
			}
		}
		
		if (urlLeaf == null) {
			return null;
		}
		
		String urlStem = App.getSharedPreferences().getString("image_url", "");
		return urlStem + urlLeaf;
	}
}
