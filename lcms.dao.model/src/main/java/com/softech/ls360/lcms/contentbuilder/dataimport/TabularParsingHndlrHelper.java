package com.softech.ls360.lcms.contentbuilder.dataimport;

import com.softech.common.delegate.Function1WithException;
import com.softech.common.delegate.Function2WithException;
import com.softech.common.event.EventArg;
import com.softech.common.event.ICallbackHandlerWithException;
import com.softech.common.parsing.TabularDataException;
import com.softech.common.parsing.TabularDataExceptionList;
import com.softech.ls360.lcms.contentbuilder.model.ControllableNode;
import com.softech.ls360.lcms.contentbuilder.utils.StringUtil;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TabularParsingHndlrHelper {

    private static Logger logger = LoggerFactory.getLogger(TabularParsingHndlrHelper.class);

    public static class MetaData {

        private final boolean mandatory;
        private final String text;
        private final String regexToValidate;
        private Class<? extends Object> type;
        private final String validationMsg;

        public MetaData(String text, boolean mandatory, String regexToValidate, Class<? extends Object> type) {
            this(text, mandatory, regexToValidate, type, "Invalid Value");
        }

        public MetaData(String text, boolean mandatory, String regexToValidate, Class<? extends Object> type, String validationMsg) {
            super();
            this.mandatory = mandatory;
            this.text = text;
            this.regexToValidate = regexToValidate;
            this.type = type;
            this.validationMsg = validationMsg;
        }

        public boolean isMandatory() {
            return mandatory;
        }

        public String getText() {
            return text;
        }

        public String getRegexToValidate() {
            return regexToValidate;
        }

        public Class<? extends Object> getType() {
            return type;
        }

        public String getValidationMsg() {
            return validationMsg;
        }

    }

    public static <N extends ControllableNode> N getNode(Map<String, N> nodesMap, String key, N newNode) {
        key = key.toLowerCase();
        ControllableNode node = nodesMap.get(key);
        if (node == null) {
            node = newNode;
            nodesMap.put(key, newNode);
        }
        return (N) node;
    }

    public static void invalidateNode(ControllableNode node) {
        while (node != null) {
            node.setError(true);
            node.setAction(null);
            try {
                node = node.getParent();
            } catch (UnsupportedOperationException ex) {
                break;
            }
        }
    }

    public static boolean isNodePopulated(ControllableNode node) {
        return (node != null && !StringUtils.isEmpty(node.getAction()) && !node.isError());
    }

    public static <N extends ControllableNode> boolean addOnPopulateHandler(N node, String handlerKey, ICallbackHandlerWithException<N, TabularDataException> handler,
            Map<String, List<ICallbackHandlerWithException<N, TabularDataException>>> handlers) throws TabularDataException {
        handlerKey = handlerKey.toLowerCase();
        boolean isPopulated = isNodePopulated(node);
        String key = node.getNodeKey();
        if (isPopulated) {
            handler.handle(new EventArg<>(key, node));
        } else if (!node.isError()) {
            addCallbackHandler(handlerKey, handler, handlers);
            return true;
        }
        return false;
    }

    public static <N extends ControllableNode> void executePopulator(int rowNum, N node, String tableName,
            Function1WithException<N, Void, TabularDataException> populator) throws TabularDataException {
        try {
            populator.apply(node);
        } catch (TabularDataException ex) {
            throw new TabularDataException(ex.getMessage(), tableName, rowNum, 0, ex.getColumnName(), ex.getErrorText());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new TabularDataException("Unknown", tableName, rowNum, 0, "", ex.getMessage());
        }

    }

    public static String getActionValue(Object[] row) {
        String displayAction = (String) row[0];
        if (StringUtils.isEmpty(displayAction) || displayAction.equalsIgnoreCase("<blank>")) {
            return "child";
        } else if (displayAction.equalsIgnoreCase("edit")) {
            return "update";
        } else if (displayAction.equalsIgnoreCase("new")) {
            return "add";
        }

        return displayAction;

    }

    public static boolean isEmptyRow(MetaData[] metaData, Object[] row) {
        boolean emptyRow = true;
        for (short i = 0; i < metaData.length; i++) {
            emptyRow &= StringUtils.isEmpty(row[i]);
        }

        return emptyRow;
    }

    public static <DTO extends ControllableNode> boolean handleRow(ITabularParsingSubHandler<DTO> parsingHandler, String tableName, MetaData[] metaData, Object[] row, int rowNum,
            Map<String, List<ICallbackHandlerWithException<DTO, TabularDataException>>> handlers,
            List<Function2WithException<DTO, Object, Object, TabularDataException>> validators,
            Function1WithException<DTO, Void, TabularDataException> dtoPopulator,
            String[] validActions
    ) throws TabularDataException {
        boolean continued = true;
        String action = getActionValue(row);
        if (rowNum == 0) {
            validateHeader(tableName, metaData, row);
        } else if (isEmptyRow(metaData, row)) {
            continued = false;
        } else if (StringUtil.equalsAny(action, true, validActions)) {
            if (StringUtil.equalsAny(action, true, "delete", "child")) {
                parsingHandler.validateRowKey(row, rowNum);
                DTO dto = parsingHandler.getDtoByRowKey(row, rowNum);
                dto.setAction(action);
                try {
                    executeValidators(rowNum, tableName, dto, validators);
                    if (dtoPopulator != null) {
                        executePopulator(rowNum, dto, tableName, dtoPopulator);
                    }
                } catch (Exception ex) {
                    invalidateNode(dto);

                    //children should not be further notified.
                    handlers.remove(parsingHandler.getHandlerKey(dto));
                    throw ex;
                }
                parsingHandler.listenParentsToGetPopulated(dto, rowNum);
            } else if (StringUtil.equalsAny(action, true, "add", "update")) {
                parsingHandler.validateRowData(row, rowNum);
                DTO dto = parsingHandler.getDtoByRowKey(row, rowNum);
                dto.setAction(action);
                try {
                    parsingHandler.addRowDataToDTO(row, rowNum, dto);
                    executeValidators(rowNum, tableName, dto, validators);
                } catch (Exception ex) {
                    invalidateNode(dto);

                    //children should not be further notified.
                    handlers.remove(parsingHandler.getHandlerKey(dto));
                    throw ex;
                }
                parsingHandler.listenParentsToGetPopulated(dto, rowNum);
            }
        }
        return continued;
    }

    public static void validateHeader(String sectionName, MetaData[] metaData, Object[] header) throws TabularDataException {
        TabularDataExceptionList exceptionList = new TabularDataExceptionList();

        for (int i = 0; i < metaData.length; i++) {
            try {
                if (StringUtils.isEmpty(header[i])) {
                    throw new TabularDataException("Empty Column", sectionName, 0, true, i, metaData[i].getText(), null);
                } else if (header[i].toString().toLowerCase().indexOf(metaData[i].getText().trim().toLowerCase()) != 0) {
                    throw new TabularDataException("Invalid Column", sectionName, 0, true, i, metaData[i].getText(), header[i].toString());
                }
            } catch (TabularDataException ex) {
                exceptionList.addException(ex);
            } catch (Exception ex) {
                exceptionList.addException(new TabularDataException("Unknown", sectionName, 0, true, i, metaData[i].getText(), ex.getMessage()));
                logger.error(ex.getMessage(), ex);
            }
        }

        if (exceptionList.hasError()) {
            throw exceptionList;
        }
    }

    public static boolean validateRow(String sectionName, MetaData[] metaData, Object[] row, int rowIndex, short firstColumn) throws TabularDataException {
        return validateRow(sectionName, metaData, row, rowIndex, firstColumn, (short) (metaData.length - 1));
    }

    public static boolean validateRow(String sectionName, MetaData[] metaData, Object[] row, int rowIndex, short firstColumn, short lastColumn) throws TabularDataException {

        TabularDataExceptionList exceptionList = new TabularDataExceptionList();

        //data validation
        for (short i = firstColumn; i <= lastColumn; i++) {
            try {

                if (StringUtils.isEmpty(row[i])) {
                    if (metaData[i].isMandatory()) {
                        throw new TabularDataException("Mandatory Field", sectionName, rowIndex, i, metaData[i].getText(), null);
                    }
                } else if (metaData[i].getType() == String.class) {
                    if (!StringUtils.isEmpty(metaData[i].getRegexToValidate()) && !Pattern.matches(metaData[i].getRegexToValidate(), row[i].toString())) {
                        throw new TabularDataException(metaData[i].getValidationMsg(), sectionName, rowIndex, i, metaData[i].getText(), row[i].toString());
                    }
                } else if (!metaData[i].getType().isAssignableFrom(row[i].getClass())) {
                    if (StringUtils.isEmpty(metaData[i].getRegexToValidate())
                            || (!StringUtils.isEmpty(metaData[i].getRegexToValidate()) && !Pattern.matches(metaData[i].getRegexToValidate(), row[i].toString()))) {
                        throw new TabularDataException(metaData[i].getValidationMsg(), sectionName, rowIndex, i, metaData[i].getText(), row[i].toString());
                    }
                }
            } catch (TabularDataException ex) {
                exceptionList.addException(ex);
            } catch (Exception ex) {
                exceptionList.addException(new TabularDataException("Unknown", sectionName, rowIndex, i, metaData[i].getText(), ex.getMessage()));
                logger.error(ex.getMessage(), ex);
            }
        }
        if (exceptionList.hasError()) {
            throw exceptionList;
        }

        return true;
    }

    public static <T> void addCallbackHandler(String key, ICallbackHandlerWithException<T, TabularDataException> handler, Map<String, List<ICallbackHandlerWithException<T, TabularDataException>>> to) {
        List<ICallbackHandlerWithException<T, TabularDataException>> handlers = to.get(key);
        if (handlers == null) {
            handlers = new ArrayList<>();
            to.put(key, handlers);
        }
        handlers.add(handler);
    }

    public static <Dto> void executeValidators(int rowNum, String tableName, Dto dto, List<Function2WithException<Dto, Object, Object, TabularDataException>> validators) throws TabularDataException {
        TabularDataExceptionList exceptionList = new TabularDataExceptionList();
        if (validators != null) {
            Object lastReturn = null;
            for (Function2WithException<Dto, Object, Object, TabularDataException> validator : validators) {
                try {
                    lastReturn = validator.apply(dto, lastReturn);
                } catch (TabularDataException ex) {
                    lastReturn = ex;
                    exceptionList.addException(new TabularDataException(ex.getMessage(), tableName, rowNum, 0, ex.getColumnName(), ex.getErrorText()));
                } catch (Exception ex) {
                    lastReturn = ex;
                    exceptionList.addException(new TabularDataException("Unknown", tableName, rowNum, 0, "", ex.getMessage()));
                    logger.error(ex.getMessage(), ex);
                }
            }
        }

        if (exceptionList.hasError()) {
            throw exceptionList;
        }
    }

    public static <T> void notifyCallbackHandlers(String key, T data, Map<String, List<ICallbackHandlerWithException<T, TabularDataException>>> from) throws TabularDataException {
        TabularDataExceptionList exceptionList = new TabularDataExceptionList();
        List<ICallbackHandlerWithException<T, TabularDataException>> handlers = from.get(key);
        if (handlers != null) {
            for (ICallbackHandlerWithException<T, TabularDataException> handler : handlers) {
                try {
                    handler.handle(new EventArg<>(key, data));
                } catch (TabularDataException ex) {
                    exceptionList.addException(ex);
                }
            }
            from.remove(key);
        }

        if (exceptionList.hasError()) {
            throw exceptionList;
        }
    }

    public static <T> void notifyCallbackHandlersWithError(String key, T data, Exception exception, Map<String, List<ICallbackHandlerWithException<T, TabularDataException>>> from) throws TabularDataException {
        TabularDataExceptionList exceptionList = new TabularDataExceptionList();
        List<ICallbackHandlerWithException<T, TabularDataException>> handlers = from.get(key);
        if (handlers != null) {
            for (ICallbackHandlerWithException<T, TabularDataException> handler : handlers) {
                try {
                    handler.error(exception, new EventArg<>(key, data));
                } catch (TabularDataException ex) {
                    exceptionList.addException(ex);
                }
            }
            from.remove(key);
        }

        if (exceptionList.hasError()) {
            throw exceptionList;
        }
    }

    public static <T> void notifyAllCallbackHandlersWithError(Map<String, List<ICallbackHandlerWithException<T, TabularDataException>>> from) throws TabularDataException {
        TabularDataExceptionList exceptionList = new TabularDataExceptionList();
        for (Object key : from.keySet().toArray()) {
            try {
                notifyCallbackHandlersWithError((String) key, null, null, from);
            } catch (TabularDataException ex) {
                exceptionList.addException(ex);
            }
        }

        if (exceptionList.hasError()) {
            throw exceptionList;
        }
    }

}
