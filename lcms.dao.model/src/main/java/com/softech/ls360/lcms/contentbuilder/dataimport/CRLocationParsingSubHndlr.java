package com.softech.ls360.lcms.contentbuilder.dataimport;

import com.softech.common.delegate.Function1WithException;
import com.softech.common.delegate.Function2WithException;
import com.softech.common.event.ICallbackHandlerWithException;
import com.softech.common.parsing.TabularDataException;
import com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.MetaData;
import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.addOnPopulateHandler;
import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.getNode;
import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.notifyAllCallbackHandlersWithError;
import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.notifyCallbackHandlers;
import java.util.HashMap;
import java.util.Map;
import static com.softech.ls360.lcms.contentbuilder.dataimport.TabularParsingHndlrHelper.validateRow;

import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.softech.ls360.lcms.contentbuilder.model.LocationDTO;
import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class CRLocationParsingSubHndlr implements ITabularParsingSubHandler<LocationDTO> {

    private static final Logger logger = LoggerFactory
            .getLogger(CRLocationParsingSubHndlr.class);

    private enum ColumnIndexes {
        ACTION,
        LOCATION_NAME,
        ADDRESS,
        CITY,
        ZIP,
        COUNTRY,
        STATE,
        PHONE,
        DESCRIPTION;
    }

    private static final MetaData[] metaData = new MetaData[]{
        new MetaData("Action", false, null, String.class),
        new MetaData("Location name", true, null, String.class),
        new MetaData("Address", true, null, String.class),
        new MetaData("City", true, null, String.class),
        new MetaData("Zip/Postal", true, null, String.class),
        new MetaData("Country", true, null, String.class),
        new MetaData("State", true, null, String.class),
        new MetaData("Phone", false, null, String.class),
        new MetaData("Description", false, null, String.class)
    };

    private final Map<String, LocationDTO> locations = new HashMap<>();
    private final List<Function2WithException<LocationDTO, Object, Object, TabularDataException>> externalValidators = new ArrayList<>();
    private final Map<String, List<ICallbackHandlerWithException<LocationDTO, TabularDataException>>> populateHandlers = new HashMap<>();
    private Function1WithException<LocationDTO, Void, TabularDataException> externalPopulator;

    public LocationDTO getLocation(String locationName) {
        LocationDTO loc = new LocationDTO();
        loc.setLocationName(locationName);
        return (LocationDTO) getNode(locations, locationName, loc);
        
    }

    @Override
    public boolean finalize(String tableName) throws TabularDataException {
        if (populateHandlers.size() > 0) {
            notifyAllCallbackHandlersWithError(populateHandlers);
            return false;
        }
        return true;
    }

    @Override
    public boolean handleRow(String tableName, Object[] row, int rowNum) throws TabularDataException {
        return TabularParsingHndlrHelper.handleRow(this, tableName, metaData, row, rowNum, populateHandlers, externalValidators, externalPopulator
                , new String[]{"child","add","update"});
    }

    @Override
    public boolean addRowDataToDTO(Object[] row, int rowNum, LocationDTO loc) throws TabularDataException {
        loc.setCountry(TypeConvertor.AnyToString(row[ColumnIndexes.COUNTRY.ordinal()]));
        loc.setCity(TypeConvertor.AnyToString(row[ColumnIndexes.CITY.ordinal()]));
        loc.setState(TypeConvertor.AnyToString(row[ColumnIndexes.STATE.ordinal()]));
        loc.setZip(StringUtil.trimSuffix(TypeConvertor.AnyToString(row[ColumnIndexes.ZIP.ordinal()]),".0"));
        loc.setStreetAddress(TypeConvertor.AnyToString(row[ColumnIndexes.ADDRESS.ordinal()]));
        loc.setPhone(StringUtil.trimSuffix(TypeConvertor.AnyToString(row[ColumnIndexes.PHONE.ordinal()]),".0"));
        loc.setDescription(TypeConvertor.AnyToString(row[ColumnIndexes.DESCRIPTION.ordinal()]));
        return true;
    }

    @Override
    public String getHandlerKey(LocationDTO loc) {
        return loc.getLocationName().toLowerCase();
    }
    
    @Override
    public LocationDTO getDtoByRowKey(Object[] row, int rowNum) throws TabularDataException {
        return getLocation(TypeConvertor.AnyToString(row[ColumnIndexes.LOCATION_NAME.ordinal()]));
    }

    @Override
    public boolean validateRowData(Object[] row, int rowIndex) throws TabularDataException {
        boolean rowAccepted = validateRow(getName(), metaData, row, rowIndex, (short) 0);
        return rowAccepted;
    }

    @Override
    public boolean validateRowKey(Object[] row, int rowIndex) throws TabularDataException {
        boolean rowAccepted = validateRow(getName(), metaData, row, rowIndex, (short) 0, (short) 1);
        return rowAccepted;
    }

    @Override
    public void listenParentsToGetPopulated(LocationDTO loc, int rowNum) throws TabularDataException {
        notifyCallbackHandlers(loc.getLocationName().toLowerCase(), loc, populateHandlers);
    }

    @Override
    public boolean addPopulateHandler(LocationDTO loc, ICallbackHandlerWithException<LocationDTO, TabularDataException> handler) throws TabularDataException {
        return addOnPopulateHandler(loc, loc.getNodeKey(), handler, populateHandlers);
    }
    
    @Override
    public String getName() {
        return "Location";
    }

    public Map<String, LocationDTO> getLocations() {
        return locations;
    }

    public List<Function2WithException<LocationDTO, Object, Object, TabularDataException>> getExternalValidators() {
        return externalValidators;
    }

}
