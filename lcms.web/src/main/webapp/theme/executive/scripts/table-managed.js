var TableManaged = function () {

    return {
        instance: null,
        hide_able_column: function (tableId) {

            /*
             Generate drop-down list
             */
            var elm = '<div class="btn-group">' +
                    '<a class="btn btn-default" href="#" data-toggle="dropdown">' +
                    'Columns ' +
                    '<i class="icon-angle-down"></i>' +
                    '</a>' +
                    '<div id="search_table_filters" class="dropdown-menu hold-on-click dropdown-checkboxes pull-right">' +
                    '<label><input type="checkbox" checked data-column="0">Title</label>' +
                    '<label><input type="checkbox" checked data-column="1">Course ID</label>' +
                    '<label><input type="checkbox" checked data-column="2">Last Modified</label>' +
                    '<label><input type="checkbox" checked data-column="3">Course Status</label>' +
                    '<label><input type="checkbox" checked data-column="4">Publish Status</label>' +
                    '<label><input type="checkbox" checked data-column="5">Course Group</label>' +
                    '<label><input type="checkbox" checked data-column="6">Rating</label>' +
                    '</div></div>';

            $(tableId + '_wrapper .table-tools-bar').append('<div class="btn-group pull-right">' + elm + '</div>');

            /*
             Drop-down on click  
             */
            $('#search_table_filters input[type="checkbox"]').change(function () {
                // Get the DataTables object again - this is not a recreation, just a get of the object
                var iCol = parseInt($(this).attr("data-column"));
                var bVis = TableManaged.instance.fnSettings().aoColumns[iCol].bVisible;
                TableManaged.instance.fnSetColumnVis(iCol, (bVis ? false : true));
            });

            /*
             Hold drop-down on click  
             */
            $('body').on('click', '.dropdown-menu.hold-on-click', function (e) {
                e.stopPropagation();
            });
        },
        moveable: function (tableId) {

            if (!jQuery().dataTable) {
                return;
            }
            $(tableId).find(".move-up,.move-down").click(function () {
                var row = $(this).parents("tr:first");
                if ($(this).is(".move-up")) {
                    row.insertBefore(row.prev());
                } else {
                    row.insertAfter(row.next());
                }
            });
        },
        //main function to initiate the module
        init: function (tableId, searchable, sortable, addOrDelete, configuration, extention) {
            var config = configuration || {};
            if (!jQuery().dataTable) {
                return;
            }

            // begin first table
            var initData = {
                "searching": searchable,
                "ordering": sortable,
                "bDestroy": true,
                "aLengthMenu": [
                    [5, 20, 30, -1],
                    [5, 20, 30, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": config.pageSize || 5,
                "sPaginationType": "bootstrap",
                "oLanguage": {
                    "sLengthMenu": "Show _MENU_",
                    "oPaginate": {
                        "sPrevious": "Prev",
                        "sNext": "Next"
                    }
                }
                
            };
            $.extend(initData, extention);
            TableManaged.instance = $(tableId).dataTable(initData);

            if (addOrDelete) {
                $(tableId + '_wrapper .table-tools-bar').append('<div class="btn-group pull-right"><a class="btn btn-default" href="javascript:table_minus_plus(\'PLUS\');" title="Add Lesson"><i class="icon-plus blue-text"></i></a><a class="btn btn-default" href="javascript:table_minus_plus(\'MINUS\');" title="Delele Lesson"><i class="icon-minus red-text"></i></a></div>');
            }

            if (TableManaged.tool_html != "null")
            {
                $(tableId + '_wrapper .table-tools-bar').prepend(TableManaged.tool_html);
            }

            jQuery(tableId + '_wrapper .dataTables_filter input').addClass("form-control-search"); // modify table search input
            jQuery(tableId + '_wrapper .dataTables_length select').addClass("form-control"); // modify table per page dropdown
            //jQuery('#sample_1_wrapper .dataTables_filter input').addClass("form-control"); // modify table search input
            //jQuery('#sample_1_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

            TableManaged.tool_html = "null";
            return TableManaged.instance;
        },
        tool_html: "null"

    };

}();