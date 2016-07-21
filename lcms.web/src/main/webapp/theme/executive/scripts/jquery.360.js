/**
 * this JS for for common JQuery functionalities of application which are not dependent on any module.
 * Dependencies :
 * 		JQuery
 * 		All JQuery plugins
 * 		PLUploader for uploading in chunks
 *
 * Features :
 * 		Auto Form Submit
 * 		Auto Validation, based on HTML5 attributes
 * 		Populate DOM with JSON and vice versa (see domToModel360 and modelToDom360)
 * 		File uploader in chunks.
 */


var $360 = (function () {
//***important***
//please dont put any module specific code inside this ananymous function.
//it is for general usage and will be moved to some common place in future.

    var me = {};

    me.URL = window.URL || window.webkitURL;

    function createObject(path, obj, val) {
        var parent = obj;
        var link = null;

        var links = path.split('.');
        for (index in links) {
            link = links[index];
            if (obj[link] == null) {
                obj[link] = {};
            }
            parent = obj;
            obj = obj[link];
        }

        if (val != null) {
            parent[link] = val;
        }
        return parent[link];
    }


    function compareDate(value, element, parameter, form, attribPrefix, side) {
        //side possible values are [From,To]
        if (value.trim() === '') {
            return true;
        }

        var dateFormat = $(element).attrData360(attribPrefix + 'format', 'MM/DD/YYYY');
        var subjectDate = moment(value, dateFormat).toDate();
        var compareDate = null;


        //1- search for comparing date value
        var dateAttribValue = $(element).attrData360(attribPrefix + 'date' + side);
        if (dateAttribValue != null) {
            if (dateAttribValue === 'now') {
                compareDate = moment(moment().format(dateFormat), dateFormat).toDate();
            } else {
                compareDate = moment(dateAttribValue, dateFormat).toDate();
            }
        }

        //2- search for comparing date selector
        if (compareDate == null) {
            var selectorAttribValue = $(element).attrData360(attribPrefix + 'selector' + side);
            if (selectorAttribValue != null) {
                var selectorFormat = $(element).attrData360(attribPrefix + 'selector-format' + side, '{0}');
                var selectorDefault = $(element).attrData360(attribPrefix + 'selector-default' + side);
                if (form.find(selectorAttribValue).val().trim() === 0) {
                    //comparing date has not been provided by the user yet.
                    //so no need to apply comparison
                    return true;
                }
                compareDate = moment(form.find(selectorAttribValue).val(), dateFormat).toDate();
            }
        }


        //3- search for comparing diff in fromat years:months:days:hours:min
        if (compareDate == null) {
            var diffAttribValue = $(element).attrData360(attribPrefix + 'diff' + side);
            if (diffAttribValue != null) {
                var mnt = moment();
                var direction = (side === 'To') ? 1 : -1;
                var dateUnits = ['years', 'months', 'days', 'hours', 'minutes', 'seconds'];
                $.each(diffAttribValue.split(':'), function (index, value) {
                    if (value.trim() !== "") {
                        mnt = mnt.add(dateUnits[index], parseInt(value) * direction);
                    }
                });
                compareDate = moment(mnt.format(dateFormat), dateFormat).toDate();
            }
        }

        if (compareDate == null) {
            //no comparing attribute defined so ignore validation
            return true;
        }


        if (side === 'To') {
            return subjectDate.getTime() <= compareDate.getTime();
        } else {
            return compareDate.getTime() <= subjectDate.getTime();
        }
    }

    me.formatMessage = function(msg,params) {
        var regex = /@\[([^\]]+)\]/gi;
        var match;
        while ((match = regex.exec(msg)) !== null) {
            var fieldName = match[1];
            var enclosedFieldName = "@\\[" + fieldName + "\\]";
            msg = msg.replace(new RegExp(enclosedFieldName, "g")
                    , createObject(fieldName, params)||"");
        }
        return msg;
    }
    me.showMessage = function (response, overrideInfo, overrideError, params) {
        overrideInfo = overrideInfo || ((response.info)? WLCMS_LOCALIZED[response.info]:null);
        overrideError = overrideError || ((response.error)? WLCMS_LOCALIZED[response.error]:null);
        params = params || response.data;
        var info = (overrideInfo) ? overrideInfo : WLCMS_LOCALIZED.SUCCESS_MESSAGE;
        var error = (overrideError) ? overrideError : WLCMS_LOCALIZED.FAILED_MESSAGE;
        var mode = 1;
        var msg = info;
        if (response.error != null) {
            msg = error;
            mode = 2;
        } else if (response.warning != null) {
            msg = error;
            mode = 2;
        }
        msg = me.formatMessage(msg,params);
        var msgBarConf = {
            vType: mode,
            vMsg: msg,
            bFadeOut: true
        };
        if(mode === 2 && response.errorBarConf){
            $.extend(msgBarConf,response.errorBarConf);
        }
        TopMessageBar.displayMessageTopBar(msgBarConf);
    };

    //********jquery prototypes************
    $.fn.submit360 = function () {
        var form = this;

        if (!$(form).valid()) {
            return;
        }

        var method = $(form).attrData360('method', 'post');
        var path = $(form).attrData360('url');
        var obj = $(form).domToModel360();
        var onSuccessExp = $(form).attrData360('360-onsuccess');

        APP.AJAX({
            url: path,
            dataType: "json",
            type: method,
            data: JSON.stringify(obj),
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            success: function (response) {

                var msgDiv = "#infoMsg";
                var mode = 1;
                var msg = WLCMS_LOCALIZED.SAVE_MESSAGE;
                if (response.error != null) {
                    msg = response.error;
                    mode = 2;
                } else if (response.warning != null) {
                    msg = response.warning;
                    mode = 2;
                } else if (!(response.info || response.info === "ok")) {
                    msg = response.info;
                }

                TopMessageBar.displayMessageTopBar({vType: mode, vMsg: msg, bFadeOut: true});
                if (response.error == null) {
                    $(form).eval360(onSuccessExp);
                }

            },
            error: function (a, b, c) {
            }
        });

    };


    $.fn.domToModel360 = function () {
        var obj = {};
        var elements = $(this).find('[data-360-bind]:visible,[data-360-bind][type=hidden]').filter('input,select');
        elements.each(function () {
            createObject($(this).attrData360('360-bind'), obj, $(this).val());
        });

        return obj;
    };

    $.fn.modelToDom360 = function (model) {
        var elements = $(this).find('[data-360-bind]');
        elements.each(function () {
            var val = createObject($(this).attrData360('360-bind'), model);
            $(this).val(val);
        });
    };

    $.fn.attrData360 = function (attrName, defVal) {
        var val = $(this).attr(attrName);
        if (val == null) {
            val = $(this).attr('data-' + attrName);
        }

        if (val == null) {
            val = defVal;
        }
        return val;
    };

    $.fn.eval360 = function (exp, arg) {
        if (exp != null) {
            return eval(exp);
        }
    };
    $.fn.applyModalContent360 = function (content) {
        if (content != null) {
            $(this).find(".modal-title").html($(content).find(".modal-title").html());
            $(this).find(".modal-body").html($(content).find(".modal-body").html());
            $(this).find(".modal-footer").html($(content).find(".modal-footer").html());
        }
    };




    //jquery validator configuratons
    $(function () {

        //validator defaults
        $.validator.setDefaults({
            errorPlacement: function (error, element) {
                var container = element.parent().parent();
                errorPlaceSelector = container.attrData360('error-place');
                if (errorPlaceSelector != null) {
                    error.insertAfter(container.find(errorPlaceSelector));
                } else {
                    error.insertAfter(element);
                }

            },
            invalidHandler: function (event, validator) {
                var errors = validator.numberOfInvalids();
                if (errors) {
                    TopMessageBar.displayMessageTopBar({vType: 2, vMsg: WLCMS_LOCALIZED.VALIDATION_PLURAL_MESSAGE_TEXT_ONLY, bFadeOut: true});
                }
            }
        });


        //custom validators
        $.validator.addMethod("dt-min", function (value, element, parameter) {
            return compareDate(value, element, parameter
                    , $(element).closest('form'), 'dt-min-', 'From');
        });

        $.validator.addMethod("dt-max", function (value, element, parameter) {
            return compareDate(value, element, parameter
                    , $(element).closest('form'), 'dt-max-', 'To');
        });

        $.validator.addMethod("dt-range", function (value, element, parameter) {
            return compareDate(value, element, parameter
                    , $(element).closest('form'), 'dt-range-', 'From')
                    && compareDate(value, element, parameter
                            , $(element).closest('form'), 'dt-range-', 'To');
        });


        $.validator.addMethod("pattern", function (value, element, parameter) {
            if (value.trim() === '') {
                return true;
            }
            var exp = $(element).attrData360('pattern-exp');
            return new RegExp(exp).test(value);
        });




    });


    //************String prototypes************
    String.prototype.format360 = function () {
        var args = arguments;
        return this.replace(/{(\d+)}/g, function (match, number) {
            return typeof args[number] !== 'undefined'
                    ? args[number]
                    : match;
        });
    };


    //*********** file uploader *****************


    $.fn.uploader360 = function () {
        $(this).each(function () {
            var uploader = $(this).data("uploader");
            if (uploader == null) {
                initializeUploader(this);
            }
        });
        return $(this).data("uploader");
    };

    function initializeUploader(upload360) {

        //**for media files video and audio
        $(upload360).append("<audio id='media-file'></audio>")
                .find("#media-file")
                .on("canplaythrough", function (e) {
                    var seconds = e.currentTarget.duration;
                    var file = $(upload360).uploader360().files[0];
                    file.media.duration = seconds;
                });
        var postActivities = $(upload360).attrData360('postActivities', '').split(',');
        var requestId = $(upload360).find('.upload-request').val();
        var fileServer = $(upload360).attrData360('file-handler', 'default');
        var dropArea = $(upload360).find('.drop-area')[0];
        var uploaderLoc = $(upload360).attrData360('uploader-loc');
        var uploader = new plupload.Uploader({
            runtimes: 'html5,flash,silverlight,html4',
            browse_button: $(upload360).find('.btn-browse')[0], // you can pass an id...
            container: upload360, // ... or DOM Element itself
            url: ((uploaderLoc) ? uploaderLoc : '..') + '/upload',
            chunk_size: '1mb',
            max_retries: 3,
            dragdrop: (dropArea != null),
            drop_element: dropArea,
            multi_selection: false,
            multipart_params: {
                requestId: requestId,
                fileServer: fileServer,
                chunkSize: 1024 * 1024 //1mb in bytes
            },
            filters: {
                max_file_size: '2gb',
                mime_types: [
                    {title: "Supported files", extensions: $(upload360).attrData360("ext", "*")}
                ]
            },
            init: {
                PostInit: function () {
                },
                FilesAdded: function (up, files) {

                    up.files.splice(0, up.files.length - 1);
                    var file = up.files[0];
                    $.extend(file, {
                        image: {width: 0, height: 0},
                        media: {duration: 0}
                    });

                    if (!$(upload360).eval360($(upload360).attrData360('onupload-validateFile', "true"), {"file": file})) {
                        this.trigger('Error', {
                            code: window.plupload.FILE_EXTENSION_ERROR,
                            message: ""
                        });
                        return false;
                    }

                    $(upload360).find('.file-preview,.file-progress,.file-panel').removeClass('hide');
                    $(upload360).find('.file-preview').html(file.name + ' (' + plupload.formatSize(file.size) + ')');
                    $(upload360).find('.file-name').html(file.name);
                    $(upload360).find('.file-size').html(plupload.formatSize(file.size));
                    $(upload360).find('.file-progress').html("");
                    $(upload360).find('.uploaded-file').val('');


                    //****get file specific meta data***

                    //for image and other media related fields.
                    if ($360.URL) {
                        if (file.type.toLowerCase().indexOf('image') === 0) {
                            var img = new Image();
                            img.onload = function () {
                                file.image.width = this.width;
                                file.image.height = this.height;
                            };
                            img.src = $360.URL.createObjectURL(file.getNative());
                        } else if (file.type.toLowerCase().indexOf('audio') === 0
                                || file.type.toLowerCase().indexOf('video') === 0) {
                            $(upload360).find('#media-file').prop("src", $360.URL.createObjectURL(file.getNative()));
                        }
                    }
                    $(upload360).eval360($(upload360).attrData360('onupload-start'));
                    up.upload();
                },
                UploadProgress: function (up, file) {

                    //It is just to avoid showing 100% before getting done (1mb defined).
                    if (file.percent > 0) {
                        file.percent--;
                    }

                    $(upload360).find('.file-progress').html(file.percent + "%");
                    $(upload360).find('.file-progress').css('color', 'blue');

                    var progressBar = $(upload360).find('.progress-bar')[0];
                    if (progressBar) {
                        $(progressBar).css('width', file.percent + "%").html(file.percent + "%");
                        $(progressBar).removeClass('progress-bar-success progress-bar-danger progress-bar-info progress-bar-warning');
                        $(progressBar).addClass('progress-bar-info');
                    }
                },
                Error: function (up, err) {
                    $(upload360).find('.btn-cancel').click();
                    $(upload360).find('.file-progress').html("error");
                    $(upload360).find('.file-progress').css('color', 'red');

                    var progressBar = $(upload360).find('.progress-bar')[0];
                    if (progressBar) {
                        $(progressBar).html("error");

                        $(progressBar).removeClass('progress-bar-success progress-bar-danger progress-bar-info progress-bar-warning');
                        $(progressBar).addClass('progress-bar-danger');
                    }

                    var msg = "Oops! Some error happened during upload. Please try again to upload a file. Sorry for inconvenience.";
                    if (err.code === window.plupload.FILE_EXTENSION_ERROR) {
                    	msg = "The file selected is of an incorrect format/extension. Please provide file of valid extension.";
                    }
                    TopMessageBar.displayMessageTopBar({vType: 2, vMsg: msg, bFadeOut: true});
                },
                ChunkUploaded: function (up, file, server) {
                    var response = JSON.parse(server.response);
                    //if this optional value found in first chunk then set it to
                    // var and uploader options to make it usable for all next chunks
                    if(response.data.fileServer){
                        fileServer = response.data.fileServer;
                        var mp = uploader.getOption("multipart_params");
                        mp["fileServer"] = fileServer;
                        uploader.setOption("multipart_params", mp);
                    }
                    if (response.error != null) {
                        this.trigger('Error', {
                            code: 99990,
                            message: response.error
                        });
                    }
                },
                FileUploaded: function (up, file, server) {
                    var response = JSON.parse(server.response);
                    if (response.error != null) {
                        this.trigger('Error', {
                            code: 99990,
                            message: response.error
                        });
                        return false;
                    }

                    postActivity = up.nextPostActivity();
                    if (postActivity) {
                        up.doPostActivity(postActivity, response);
                    } else {
                        up.finalize(response);
                    }

                }
            }
        });

        uploader.init();
        $(upload360).eval360($(upload360).attrData360('onupload-init'));
        //extend uploader with custom methods.
        $.extend(uploader, {
            cancel: function () {
                $(upload360).eval360($(upload360).attrData360('onupload-cancel'));
                uploader.stop();
                $(upload360).find('.btn-cancel,.file-panel').addClass('hide');
                $(upload360).find('.btn-browse,.moxie-shim,.drop-area').removeClass('hide');
                $(upload360).find('.btn-browse').removeAttr('disabled');
                $(upload360).find('.file-progress').html("canceled");
                uploader.currentActivityIndex = 0;
            },
            reset: function () {
                $(upload360).eval360($(upload360).attrData360('onupload-reset'));
                uploader.cancel();
                $(upload360).find('.file-progress').html('');
                $(upload360).find('.btn-cancel,.file-preview,.file-progress,.btn-browse>.browse-change,.file-panel').addClass('hide');
                $(upload360).find('.btn-browse>.browse-new').removeClass('hide');
                $(upload360).find('.uploaded-file').val('');
                $(upload360).find('.moxie-shim')
                        .css('width', '92px')
                        .css('height', '34px')
                        .css('left', '15px');
            },
            upload: function () {
                uploader.start();
                $(upload360).find('.btn-browse:not(.show-always),.moxie-shim').addClass('hide');
                $(upload360).find('.btn-browse').attr('disabled', 'true');
                $(upload360).find('.btn-cancel').removeClass('hide');
                $(upload360).find('td .btn-cancel').closest('td').removeClass("hide");
                $(upload360).find('td .progress-bar').closest('td').removeClass("hide");
                $(upload360).find('.drop-area').addClass('hide');
                return false;
            },
            nextPostActivity: function (increament) {
                var index = uploader.currentActivityIndex || 0;
                if (postActivities.length > index && postActivities[index]) {
                    if (increament === undefined ||increament === null || increament === true) {
                        uploader.currentActivityIndex = index + 1;
                    }
                    return {
                        name: postActivities[index],
                        msg: $(upload360).attrData360('activity-' + postActivities[index] + '-msg', postActivities[index])
                    }
                }
            },
            finalize: function (response) {
                $(upload360).find('.btn-cancel,.btn-browse>.browse-new').addClass('hide');
                $(upload360).find('.btn-browse,.moxie-shim,.btn-browse>.browse-change,.drop-area').removeClass('hide');
                $(upload360).find('.btn-browse').removeAttr('disabled');

                $(upload360).find('.file-progress').html("done");
                $(upload360).find('.file-progress').css('color', 'green');

                $(upload360).find('.uploaded-file').val(response.data.filePath);
                uploader.currentActivityIndex = 0;
                var formId = $(upload360).closest('form').attr("id");

                var progressBar = $(upload360).find('.progress-bar')[0];
                if (progressBar) {
                    $(progressBar).html("done");

                    $(progressBar).removeClass('progress-bar-success progress-bar-danger progress-bar-info progress-bar-warning');
                    $(progressBar).addClass('progress-bar-success');
                }
                $(upload360).find('td .btn-cancel').closest('td').addClass("hide");
                $(upload360).find('td .progress-bar:not(.show-always)').closest('td').addClass("hide");


                $(upload360).eval360($(upload360).attrData360('onupload-done'));
            },
            doPostActivity: function (activity, uploadResponse, lastActivityResponse) {
                $(upload360).find('.file-progress').html(activity.msg);
                $(upload360).find('.file-progress').css('color', 'blue');

                var progressBar = $(upload360).find('.progress-bar')[0];
                if (progressBar) {
                    $(progressBar).html(activity.msg);

                    $(progressBar).removeClass('progress-bar-success progress-bar-danger progress-bar-info progress-bar-warning');
                    $(progressBar).addClass('progress-bar-info');
                }

                var fnAjax = (uploaderLoc) ? $.ajax : APP.AJAX;
                fnAjax({
                    url: ((uploaderLoc) ? uploaderLoc : '.') + "/doPostUploadActivites",
                    dataType: "json",
                    type: "post",
                    data: {
                        filePath: uploadResponse.data.filePath,
                        activity: activity.name,
                        requestId: requestId,
                        fileServer: fileServer
                    },
                    success: function (response) {
                        if (response.error != null) {
                            uploader.trigger('Error', {
                                code: 99990,
                                message: response.error
                            });
                            return false;
                        }


                        $(upload360).eval360($(upload360).attrData360('onupload-activity'), {activity: activity, uploadResponse: uploadResponse, activityResponse: response});
                        var nextPostActivity = uploader.nextPostActivity();
                        if (nextPostActivity) {
                            uploader.doPostActivity(data, response);
                        } else {
                            uploader.finalize(uploadResponse);
                        }
                    },
                    error: function (a, b, c) {
                        uploader.trigger('Error', {
                            code: 99990,
                            message: "uknown error, see server log"
                        });
                        return false;
                    }
                });
            }


        });

        $(upload360).find('.btn-cancel').click(function () {
            uploader.cancel();
        });


        //attach data with element.
        $(upload360).data("uploader", uploader);


    }

    return me;

})();

//initialize PLUploaders
$(function () {
    $('.upload-360').uploader360();
});


//Uploading related prototypes for Stirng.
String.prototype.fileName = function () {
    var fileNameIndexForward = this.lastIndexOf("/");
    var fileNameIndexBack = this.lastIndexOf("\\");
    var fileNameIndex = (fileNameIndexBack > fileNameIndexForward) ? fileNameIndexBack : fileNameIndexForward;
    var filename = this.substr(fileNameIndex + 1);
    return filename;
};

String.prototype.padLeft = function (size, padwith) {
    if (size <= this.length) {
        return this;
    } else {
        return Array(size - this.length + 1).join(padwith || '0') + this;
    }
};



