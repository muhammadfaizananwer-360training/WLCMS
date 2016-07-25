$(document).ready(function(){
	APP.PLACEHOLDER_FIX();
	APP.EDIT_OR_VIEW_TOGGLE();
	APP.LEFT_NAV.init("OPEN","nav_accordion_3");

	//auto reset value to two floating points
    $("mSRP").change(function(){
        this.value=parseFloat(element.value).toFixed(2);
    });

	//auto reset value to two floating points
    $("lowestSalePrice").change(function(){
        this.value=parseFloat(element.value).toFixed(2);
    });

	//value must be limited to 8 digits and two floationg points like 12345678.12
	jQuery.validator.addMethod("dollarsscents", function(value, element) {
        return this.optional(element) || /^(\d{0,8})*(\.?\d{0,2})$/.test(value);
    }, "You must include two decimal places");

	$('#lnkPricing a').addClass('active');

	if ($('#offerprice').val () == 0)
			{
				$('#offerprice').val("0.00");
			}


});


$(function() {
    // Setup form validation on the #register-form element


	jQuery.validator.addMethod("dollarsscentsOfferPrice", function(value, element) {
		if ($('#chkManagerOffer').is( ":checked" ) ) {
			return this.optional(element) || /^(\d{0,8})(\.\d{2})$/.test(value);
		} else {
			return true;
		}
    }, "You must include two decimal places");

	 $.validator.addMethod('minStrict', function (value, el, param) {
		    if ($('#chkManagerOffer').is( ":checked" ) ) {
				return value >= param;
			} else {
				return true;
			}
	}, "If marketing offer is checked then offer price should be greater than 0");


    $("#frm_pricing_edit").validate({

        // Specify the validation rules
        rules: {

		mSRP: {
                number: true,
			    dollarsscents: true
            },
		lowestSalePrice: {
                number: true,
                dollarsscents: true
            },
		offerprice: {
                number: true,
                dollarsscentsOfferPrice: true
				,minStrict : 0.00
            }
        },
        // Specify the validation error messages
        messages: {
        	mSRP: "Please enter a dollar amount to the nearest cent. For example, if you would like to sell your course for $10, enter \"10.00\". Do not include additional characters such as dollar signs, or separator commas in large numbers.",
      		lowestSalePrice:"Please enter a dollar amount to the nearest cent. For example, if you would like to sell your course for $10, enter \"10.00\". Do not include additional characters such as dollar signs, or separator commas in large numbers.",
			offerprice: {
				number:"Please enter a dollar amount to the nearest cent. For example, if you would like to sell your course for $10, enter \"10.00\". Do not include additional characters such as dollar signs, or separator commas in large numbers.",
				dollarsscentsOfferPrice:"Please enter a dollar amount to the nearest cent. For example, if you would like to sell your course for $10, enter \"10.00\". Do not include additional characters such as dollar signs, or separator commas in large numbers.",
				minStrict:  "If manage offer is checked then offer price should be greater than 0.00"
			}
        },

        submitHandler: function(form)
        {
			form.submit();
        }
    });

    $('#chkManagerOffer').is( ":checked" ) ? $('#priceOnStoreDiv').show()  : $('#priceOnStoreDiv').hide() ;
  });

function elementFadeOut(id)
{
	setTimeout(function(){$(id).html(''); },9000);
}

function onOfferPrice () {
	$('#chkManagerOffer').is( ":checked" ) ? $('#priceOnStoreDiv').show()  : $('#priceOnStoreDiv').hide() ;
	$('#chkManagerOffer').is( ":checked" ) ? $('#offerprice').attr("readonly", false) : $('#offerprice').attr("readonly", true);
	if($('#chkManagerOffer').is( ":checked" )){
		$('#offerprice').val("0.00");
		$('#offerprice').attr("required", true);
		$('#offerprice').attr("data-msg-required", "Please enter price");

	}
}
