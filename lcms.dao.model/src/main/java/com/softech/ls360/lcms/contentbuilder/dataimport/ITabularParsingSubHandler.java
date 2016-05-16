package com.softech.ls360.lcms.contentbuilder.dataimport;

import com.softech.common.event.ICallbackHandlerWithException;
import com.softech.common.parsing.TabularDataException;
import com.softech.ls360.lcms.contentbuilder.model.ControllableNode;

public interface ITabularParsingSubHandler<DTO extends ControllableNode> {

    boolean handleRow(String tableName, Object[] row, int rowNum) throws TabularDataException;
    boolean finalize(String tableName) throws TabularDataException;
    String getName();
    
    boolean addRowDataToDTO(Object[] row, int rowNum, DTO dto) throws TabularDataException;
    DTO getDtoByRowKey(Object[] row, int rowNum) throws TabularDataException;
    boolean validateRowData(Object[] row, int rowNum) throws TabularDataException;
    boolean validateRowKey(Object[] row, int rowNum) throws TabularDataException;
    void listenParentsToGetPopulated(DTO dto,int rowNum) throws TabularDataException;
    boolean addPopulateHandler(DTO dto,ICallbackHandlerWithException<DTO, TabularDataException> handler) throws TabularDataException;
    String getHandlerKey(DTO dto);
}
