var fromSelectPlan = false;/*done*/
var fromRedeemPIN = false;/*done*/
var onCC = false;/*done*/
var isUpdateDataUsage = false;/*done*/
/*done*/var viewportSupportedUserAgent = /Android|webOS|BlackBerry/i;  // Note we keep iPhone|iPad|iPod| specifically out. Enabling the viewport on those browser disables zoom in/out which is bad.
/*done*/var isApplePad = /iPhone|iPad|iPod/i;
/*done*/
$(window).resize(function(){
    var top;
	var left;
	$(".ui-dialog").each(function(index){ 
		var top = -($(this).height() / 2);
		var left = -($(this).width() / 2);		
		
		// limit the scroll up to 0 so we won't go into negative and lose scroll up to important area
		if ($(this).position().top + top < 0) {
			top = -($(this).position().top);
		}
		if ($(this).position().left + left < 0) {
			left = -($(this).position().left);
		}
		
		$(this).css({"position": "absolute", "margin-left": left+"px", "margin-top": top+"px", "left": "50%", "top": "50%" });
	});
	setFooterLeft(113);
});
$(function(){
	/*
	if(!($.browser.msie)) {
		$("head style").load($("#cssForm").attr('action'), function(response, status, request){
		});
	}
	*/
	
	if( isApplePad.test(navigator.userAgent) ) {
		window.scrollTo(0, 10);
		window.addEventListener('orientationchange', updateApplePadOrientation, false);
	}
	
	if( viewportSupportedUserAgent.test(navigator.userAgent) ) {
		var viewPortTag=document.createElement('meta');
		viewPortTag.id="viewport";
		viewPortTag.name = "viewport";
		viewPortTag.content = "initial-scale=0.4, user-scalable=yes";
		document.getElementsByTagName('head')[0].appendChild(viewPortTag);
		window.addEventListener('orientationchange', updateOrientation, false);
	}
	
	// Avoid `console` errors in browsers that lack a console.
	// Following conditional handles Windows IE when console object is not supported,
	// thus evading any errors (seen and unseen) from within the DOM.
	if (!(window.console && console.log)) {
	  (function() {
	    var noop = function() {};
	    var methods = ['assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error', 'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log', 'markTimeline', 'profile', 'profileEnd', 'markTimeline', 'table', 'time', 'timeEnd', 'timeStamp', 'trace', 'warn'];
	    var length = methods.length;
	    var console = window.console = {};
	    while (length--) {
	        console[methods[length]] = noop;
	    }
	  }());
	}/*done*/
	$.ajaxSetup({ cache: false });/*done*/
	handleBookmark();/*done*/
	
	// Initial center the div horizontally in respect to browser window width
	/*done*/
	$(".footer").css({"left": ($(window).width() - $('.footer').outerWidth()) / 2 + "px"});
	
	$(".more-info").mouseover(function() {
		$(this).parent().find('.plan-tip').show();
	}).mouseout(function() { 
		$(this).parent().find('.plan-tip').hide();
	});
	
	$("head style").load($("#cssForm").attr('action'), function(response, status, request){
	});/*done*/
	$(".button").button();/*done*/
	$("button").button();/*done*/
	$(".numeric").numeric();/*done*/
	/*done*/
	$.each(countryISO, function(index, map) {   
	     $('#ccCountry').append($('<option>', {"value":map.cca2}).text(map.name)); 
	});
	/*done*/
	$("#dialog").dialog({
	    autoOpen: false,
		height: 570,
		width: 920,
		/*modal: true,*/
		draggable: false,
		resizable: false,
		closeOnEscape: false,
		show: "fade"
	});
	/*done*/
	$("#dialog-login").dialog({
	    autoOpen: false,
		height: 250,
		width: 375,
		modal: true,
		draggable: false,
		resizable: false,
		closeOnEscape: false,
		show: "fade"
	});
	/*done*/
	$("#dialog-signup").dialog({
	    autoOpen: false,
		height: 500,
		width: 450,
		modal: true,
		draggable: false,
		resizable: false,
		closeOnEscape: false,
		show: "fade"
	});
	/*done*/
	$("#planSelectFilter").dialog({
	    autoOpen: false,
	    height: 250,
		width: 375,
		modal: true,
		draggable: false,
		resizable: false,
		closeOnEscape: true,
		show: "fade"
	});
	/*done*/
	$("#dialog-datameter").dialog({
	    autoOpen: false,
	    height: 250,
		width: 375,
		modal: true,
		draggable: false,
		resizable: false,
		closeOnEscape: true,
		show: "fade"
	});
	/*done*/
	$("#dialog-addon-declined").dialog({
	    autoOpen: false,
	    height: 250,
		width: 375,
		modal: true,
		draggable: false,
		resizable: false,
		closeOnEscape: true,
		show: "fade"
	});
	/*done*/
	$("#dialog-ajax-error").dialog({
	    autoOpen: false,
	    height: "auto",
		width: 375,
		modal: true,
		draggable: false,
		resizable: false,
		closeOnEscape: true,
		show: "fade"
	});
	/*done*/
	$("#redeemPIN-auth").dialog({
	    autoOpen: false,
	    height: "auto",
		width: 375,
		modal: true,
		draggable: false,
		resizable: false,
		closeOnEscape: true,
		show: "fade"
	});
	/*done*/
	$("#redeemPIN-status").dialog({
	    autoOpen: false,
	    height: "auto",
		width: 375,
		modal: true,
		draggable: false,
		resizable: false,
		closeOnEscape: true,
		show: "fade"
	});
	/*done*/
	$("#payment-confirm").dialog({
	    autoOpen: false,
	    height: "auto",
		width: 375,
		modal: true,
		draggable: false,
		resizable: false,
		closeOnEscape: true,
		show: "fade"
	});
	/*done*/
	$("#overlay").dialog({
	    autoOpen: false,
		height: 570,
		width: 920,
		draggable: false,
		resizable: false,
		closeOnEscape: false
	});
	/*done*/
	$("#dialog-contact").dialog({
	    autoOpen: false,
		height: "auto",
		width: 375,
		modal: true,
		draggable: false,
		resizable: false,
		closeOnEscape: true,
		show: "fade"
	});
	/*done*/
	$("#tc-content").load($("#tcContentForm").attr('action'), function(response, status, request){
    	//setup();
	});
	/*done*/
	$("#loginForm").validate({
		submitHandler: function(form) {
		    submitLoginForm();
		},
	    rules: {
	    	userId: {
	    		required: true,
	    		minlength: 3,
	    		maxlength: 100
	    	},
	    	passwordLogin: {
	    		required: true,
	    		minlength: 6,
	    		maxlength: 10
	    	}
	    },
	    messages: {
	        userId: {
	        	required: "User Name is required.",
	        	minlength: "User Name cannot be less than 3 characters.",
	    		maxlength: "User Name cannot be more than 100 characters."
	        },
	    	passwordLogin: {
	            required: "Password is required.",
	            minlength: "Password cannot be less than 6 characters.",
	    		maxlength: "Password cannot be more than 10 characters."
	        }
	    },
	    //errorContainer: "#login-error-validation",
	    errorPlacement: function(error, element) {
	    	$(element).attr('title', error.text());
	    	$(element).parent("td").next("td").children("span").attr('title', error.text());
	    	$(element).parent("td").next("td").children("span").fadeIn();
	        return true;
	    },
		unhighlight: function(element, errorClass, validClass) {
		    $(element).removeClass(errorClass).addClass(validClass);
		    $(element).parent("td").next("td").children("span").removeAttr('title');
			$(element).removeAttr('title');
	    	$(element).parent("td").next("td").children("span").fadeOut();
		}
	    /*,
	    focusCleanup: true,
	    focusInvalid: false*/
	});
	/*done*/
	$("#signupForm").validate({
		submitHandler: function(form) {
		    submitSignupForm();
		},
	    rules: {
	    	userId: {
	    		required: true,
	    		minlength: 3,
	    		maxlength: 100
	    	},
	    	password: {
	    		required: true,
	    		minlength: 6,
	    		maxlength: 10
	    	},
	    	passwordConfirm: {
	    		required: true,
	    		equalTo: "#password",
	    		minlength: 6,
	    		maxlength: 10
	    	},
	    	securityQuestionId: {
	    		required: true
	    	},
	    	securityAnswer: {
	    		required: true,
	    		maxlength: 100
	    	},
	    	email: {
	    		required: true,
	    		email: true,
	    		maxlength: 100
	    	},
	    	firstName: {
	    		maxlength: 30
	    	},
	    	lastName: {
	    		maxlength: 30
	    	},
	    	loyaltyReference: {
	    		maxlength: 16
	    	}
	    },
	    messages: {
	    	userId: {
	    		required: "User Name is required.",
	        	minlength: "User Name cannot be less than 3 characters.",
	    		maxlength: "User Name cannot be more than 100 characters."
	    	},
	    	password: {
	    		required: "Password is required.",
	            minlength: "Password cannot be less than 6 characters.",
	    		maxlength: "Password cannot be more than 10 characters."
	    	},
	    	passwordConfirm: {
	    		required: "Confirm Password is required.",
	    		equalTo: "Password does not match.",
	    		minlength: "Password cannot be less than 6 characters.",
	    		maxlength: "Password cannot be more than 10 characters."
	    	},
	    	securityQuestionId: {
	    		required: "Security Question is required."
	    	},
	    	securityAnswer: {
	    		required: "Security Answer is required.",
	    		maxlength: "Security Answer cannot be more than 100 characters."
	    	},
	    	email: {
	    		required: "Email Address is required.",
	    		email: "Email Address is invalid.",
	    		maxlength: "Email Address cannot be more than 100 characters."
	    	},
	    	firstName: {
	    		maxlength: "First Name cannot be more than 30 characters."
	    	},
	    	lastName: {
	    		maxlength: "Last Name cannot be more than 30 characters."
	    	},
	    	loyaltyReference: {
	    		maxlength: "Rewards Number cannot be more than 16 characters."
	    	}
	    },
	    //errorContainer: "#signup-error-validation",
	    errorPlacement: function(error, element) {
	    	$(element).attr('title', error.text());
	    	$(element).parent("td").next("td").children("span").attr('title', error.text());
	    	$(element).parent("td").next("td").children("span").fadeIn();
	        return true;
	    },
		unhighlight: function(element, errorClass, validClass) {
		    $(element).removeClass(errorClass).addClass(validClass);
		    $(element).parent("td").next("td").children("span").removeAttr('title');
		    $(element).removeAttr('title');
	    	$(element).parent("td").next("td").children("span").fadeOut();
		}
	});
	/*done*/
	$("#forgetpasswordForm").validate({
		submitHandler: function(form) {
		    submitForgetpasswordForm();
		},
	    rules: {
	    	userId: {
	    		required: true,
	    		minlength: 3,
	    		maxlength: 100
	    	}
	    },
	    messages: {
	    	userId: {
	    		required: "User Name is required.",
	        	minlength: "User Name cannot be less than 3 characters.",
	    		maxlength: "User Name cannot be more than 100 characters."
	    	}
	    },
	    //errorContainer: "#forgetpassword-error-validation",
	    errorPlacement: function(error, element) {
	    	$(element).attr('title', error.text());
	    	$(element).parent("td").next("td").children("span").attr('title', error.text());
	    	$(element).parent("td").next("td").children("span").fadeIn();
	        return true;
	    },
		unhighlight: function(element, errorClass, validClass) {
		    $(element).removeClass(errorClass).addClass(validClass);
		    $(element).parent("td").next("td").children("span").removeAttr('title');
		    $(element).removeAttr('title');
	    	$(element).parent("td").next("td").children("span").fadeOut();
		}
	});
	/*done*/
	$("#resetpasswordForm").validate({
		submitHandler: function(form) {
		    submitResetpasswordForm();
		},
	    rules: {
	    	securityAnswer: {
	    		required: true,
	    		maxlength: 100
	    	},
	    	passwordNew: {
	    		required: true,
	    		minlength: 6,
	    		maxlength: 10
	    	},
	    	passwordNewConfirm: {
	    		required: true,
	    		equalTo: "#passwordNew",
	    		minlength: 6,
	    		maxlength: 10
	    	}
	    },
	    messages: {
	    	securityAnswer: {
	    		required: "Security Answer is required.",
	    		maxlength: "Security Answer cannot be more than 100 characters."
	    	},
	    	passwordNew: {
	    		required: "New Password is required.",
	            minlength: "New Password cannot be less than 6 characters.",
	    		maxlength: "New Password cannot be more than 10 characters."
	    	},
	    	passwordNewConfirm: {
	    		required: "Confirm New Password is required.",
	    		equalTo: "New Password does not match.",
	    		minlength: "New Password cannot be less than 6 characters.",
	    		maxlength: "New Password cannot be more than 10 characters."
	    	}
	    },
	    //errorContainer: "#resetpassword-error-validation",
	    errorPlacement: function(error, element) {
	    	$(element).attr('title', error.text());
	    	$(element).parent("td").next("td").children("span").attr('title', error.text());
	    	$(element).parent("td").next("td").children("span").fadeIn();
	        return true;
	    },
		unhighlight: function(element, errorClass, validClass) {
		    $(element).removeClass(errorClass).addClass(validClass);
		    $(element).parent("td").next("td").children("span").removeAttr('title');
		    $(element).removeAttr('title');
	    	$(element).parent("td").next("td").children("span").fadeOut();
		}
	});
	/*done*/
	$("#ccForm").validate({
		submitHandler: function(form) {
		    confirmPayment();
		},
	    rules: {
	    	ccFullName: {
	    		required: "#add-cc-table:visible"
	    	},
	    	ccAddressLine1: {
	    		required: "#add-cc-table:visible"
	    	},
	    	ccCityTown: {
	    		required: "#add-cc-table:visible"
	    	},
	    	ccStateProvinceRegion: {
	    		required: function(element) {
					return ("US"==$("#ccCountry").val());
				},
	    		minlength: {
    				param: "2",
    				depends: function(element) {
    					return ("US"==$("#ccCountry").val());
    				}
    			},
    			maxlength: {
    				param: "2",
    				depends: function(element) {
    					return ("US"==$("#ccCountry").val());
    				}
    			}
	    	},
	    	ccCountry: {
	    		required: "#add-cc-table:visible"
	    	},
	    	creditCardNumber: {
	    		required: true,
	    		minlength: 13,
	    		maxlength: 16
	    	},
	    	creditCardExpirationMonth: {
	    		required: true,
	    		min: 1,
	    		max: 12
	    	},
	    	creditCardExpirationYear: {
	    		required: true,
	    		min: 13,
	    		max: 99
	    	},
	    	creditCardZip: {
	    		minlength: 3,
	    		maxlength: 10
	    	},
	    	creditCardSecurityCode: {
	    		required: true,
	    		minlength: 3,
	    		maxlength: 4
	    	}
	    },
	    messages: {
	    	ccFullName: {
	    		required: "Full Name is required."
	    	},
	    	ccAddressLine1: {
	    		required: "Address Line 1 is required."
	    	},
	    	ccCityTown: {
	    		required: "City/Town is required."
	    	},
	    	ccStateProvinceRegion: {
	    		required: "State in US is required.",
	    		minlength: "State in US cannot be less than 2 characters.",
	    		maxlength: "State in US cannot be more than 2 characters."
	    	},
	    	ccCountry: {
	    		required: "Country is required."
	    	},
	    	creditCardNumber: {
	    		required: "Card Number is required.",
	    		minlength: "Card Number cannot be less than 13 digits.",
	    		maxlength: "Card Number cannot be more than 16 digits."
	    	},
	    	creditCardExpirationMonth: {
	    		required: "Card Expiration Month is required.",
	    		min: "Card Expiration Month cannot be less than 1.",
	    		max: "Card Expiration Month cannot be more than 12."
	    	},
	    	creditCardExpirationYear: {
	    		required: "Card Expiration Year is required.",
	    		min: "Card Expiration Year cannot be before 2013.",
	    		max: "Card Expiration Year cannot be after 2099."
	    	},
	    	creditCardZip: {
	    		minlength: "Postal Code is 3-10 digits and/or letters.",
	    		maxlength: "Postal Code is 3-10 digits and/or letters."
	    	},
	    	creditCardSecurityCode: {
	    		required: "Security Code is required.",
	    		minlength: "Security Code cannot be less than 3 digits.",
	    		maxlength: "Security Code cannot be more than 4 digits."
	    	}
	    },
	    //errorContainer: "#cc-error-validation",
	    errorPlacement: function(error, element) {
	    	$(element).attr('title', error.text());
	    	$(element).parent("td").next("td").children("span").attr('title', error.text());
	    	$(element).parent("td").next("td").children("span").fadeIn();
	        return true;
	    },
		unhighlight: function(element, errorClass, validClass) {
		    $(element).removeClass(errorClass).addClass(validClass);
		    $(element).parent("td").next("td").children("span").removeAttr('title');
		    $(element).removeAttr('title');
	    	$(element).parent("td").next("td").children("span").fadeOut();
		}
	});
	/*done*/
	$("#ccCountry").change(function() {
		$("#ccForm").validate().element("#ccStateProvinceRegion");
	});
	/*done*/
	$("#redeemPINForm").validate({
		submitHandler: function(form) {
			submitPINForm();
		},
	    rules: {
	    	pin: {
	    		required: true
	    	}
	    },
	    messages: {
	    	pin: {
	    		required: "PIN is required before selecting Redeem."
	    	}
	    },
	    //errorContainer: "#forgetpassword-error-validation",
	    errorPlacement: function(error, element) {
	    	$(element).attr('title', error.text());
	        return true;
	    },
		unhighlight: function(element, errorClass, validClass) {
		    $(element).removeClass(errorClass).addClass(validClass);
		    $(element).removeAttr('title');
		}
	});
	/*done*/
	$("#redeemPromoForm").validate({
		submitHandler: function(form) {
			submitPromoForm();
		},
	    rules: {
	    	promo: {
	    		required: true
	    	}
	    },
	    messages: {
	    	promo: {
	    		required: "Promotion code is required before selecting Redeem."
	    	}
	    },
	    //errorContainer: "#forgetpassword-error-validation",
	    errorPlacement: function(error, element) {
	    	$(element).attr('title', error.text());
	        return true;
	    },
		unhighlight: function(element, errorClass, validClass) {
		    $(element).removeClass(errorClass).addClass(validClass);
		    $(element).removeAttr('title');
		}
	});
	/*done*/
	$("#changePromoForm").validate({
		submitHandler: function(form) {
			submitChangePromoForm();
		},
	    rules: {
	    	changePromo: {
	    		required: true
	    	}
	    },
	    messages: {
	    	changePromo: {
	    		required: "Promotion code is required before selecting Redeem."
	    	}
	    },
	    //errorContainer: "#forgetpassword-error-validation",
	    errorPlacement: function(error, element) {
	    	$(element).attr('title', error.text());
	        return true;
	    },
		unhighlight: function(element, errorClass, validClass) {
		    $(element).removeClass(errorClass).addClass(validClass);
		    $(element).removeAttr('title');
		}
	});
	/*done - but not working*/
	$("#promo").off("keyup", "**"); 
	$("#promo").on("keyup", function(event) {
		if(this.value.length > 0) {
			$("#plan-table a").button("disable");
		} else {
			$("#plan-table a").button("enable");
		}
	});
	$("#changePromo").off("keyup", "**"); 
	$("#changePromo").on("keyup", function(event) {
		if(this.value.length > 0) {
			$("#changeplan-table a").button("disable");
		} else {
			$("#changeplan-table a").button("enable");
		}
	});
	/*done*/
	$("#tc-checkbox").on("click", function(event) {
		if($(this).is(':checked')) {
			$("#tc-buttons a:first").button("enable");
		} else {
			$("#tc-buttons a:first").button("disable");
		}
	});
	/*done*/
	$("#mop-yes").on("click", toggleMop);
	$("#mop-balance").on("click", toggleMop);
	$("#mop-bill").on("click", toggleMop);
	$("#mop-no").on("click", toggleMop);
	setTimeout(function() {
		$("#dialog").dialog("open");/*done*/
		$('.ui-dialog .button').blur();/*done*/
		// Setting .footer left position here after main dialog div loads and is available to the DOM
		setFooterLeft(113);/*done*/
		// Updating .footer
		updateDataUsage();/*done*/
		$(".footer").fadeIn();/*done*/
		$(".footer").hover( 
			function() {
				// Scroll Up
			    $('.footer').stop(true).animate({"margin-bottom": 0}, {queue: false, duration: 300});
			}, 
			function() {
				// Scroll Down
				var offset = -($(this).outerHeight()) + $("#top-box-content").outerHeight() + ($("#footer-content").outerHeight()-$("#footer-content").innerHeight())/2 + "px";
				$('.footer').stop(true).animate({"margin-bottom": offset}, {queue: false, duration: 300});
		});/*done*/
	}, 1500);/*done*/
	// Updating .footer 2x per minute
	setInterval(function() {
		updateDataUsage();
		if( viewportSupportedUserAgent.test(navigator.userAgent) ) {
			updateOrientation();
		}

	}, 30000);/*done*/
	$("#overlay").ajaxStart(function(){
		if(!isUpdateDataUsage) {
			showProgress();
		}
	});/*done*/
	$("#overlay").ajaxStop(function(){
		if(isUpdateDataUsage) {
			isUpdateDataUsage = false;
		}
		hideProgress();
	});/*done*/
	/*done*/
	$("#overlay").ajaxError(function(event, request, settings, error){
		console.log("Ajax error event (" + event + ")");
		console.log("Ajax error requst (" + request + ")");
		console.log("Ajax error URL (" + settings.url + ")");
		console.log("Ajax error (" + error + ")");
		hideProgress();
		if(isUpdateDataUsage) {
			isUpdateDataUsage = false;
		}
		else {
			showAjaxErrorDialog();
		}
	});
	if( viewportSupportedUserAgent.test(navigator.userAgent) ) {
		updateOrientation();
	}

	/* juan
	$('#loyaltyReference').hover(function(e) {
		var moveLeft = 20;
		  var moveDown = 10;
	    $('div#loyaltyPopUp').show()
	    .css('top', pageY + moveDown)
	    .css('left', e.pageX + moveLeft)
	    .appendTo('body');
	  }, function() {
	    $('div#loyaltyPopUp').hide();
	  });*/
});
/*done*/
function debugResponse(response) {
	console.log("response.authenticated = " + response.authenticated);
	console.log("response.newSignup = " + response.newSignup);
	console.log("response.showTC = " + response.showTC);
	console.log("response.newTC = " + response.newTC);
	console.log("response.name = " + response.name);
	console.log("response.securityQuestionId = " + response.securityQuestionId);
	console.log("response.securityQuestionText = " + response.securityQuestionText);
	console.log("response.supportSubscriber = " + response.supportSubscriber);
	console.log("response.supportLoyalty = " + response.supportLoyalty);
	console.log("response.supportDataMeter = " + response.supportDataMeter);
	console.log("response.allowPayByBalance = " + response.allowPayByBalance);
	console.log("response.usedBalance = " + response.usedBalance);
	console.log("response.allowBillto = " + response.allowBillto);
	console.log("response.free = " + response.free);
	console.log("response.moptype = " + response.moptype);
	console.log("response.mopcode = " + response.mopcode);
	console.log("response.due = " + response.due);
	console.log("response.dueRaw = " + response.dueRaw);
	console.log("response.purchasePriceWithTax = " + response.purchasePriceWithTax);
	console.log("response.purchasePriceWithTaxRaw = " + response.purchasePriceWithTaxRaw);
	console.log("response.stillOwe = " + response.stillOwe);
	console.log("response.promoCode = " + response.promoCode);
	console.log("response.selectedPlanName = " + response.selectedPlanName);
	console.log("response.selectedPlanPrice = " + response.selectedPlanPrice);
	console.log("response.selectedPlanPriceRaw = " + response.selectedPlanPriceRaw);
	console.log("response.selectedPlanLoyaltyPoints = " + response.selectedPlanLoyaltyPoints);
	console.log("response.selectedPlanDesc = " + response.selectedPlanDesc);
	console.log("response.selectedPlanExpiration = " + response.selectedPlanExpiration);
	console.log("response.purchaseCCLast4 = " + response.purchaseCCLast4);
	console.log("response.purchaseCCAmount = " + response.purchaseCCAmount);
	console.log("response.purchaseCCAmountRaw = " + response.purchaseCCAmountRaw);
	console.log("response.purchaseCCAuth = " + response.purchaseCCAuth);
	console.log("response.purchaseBalancePaid = " + response.purchaseBalancePaid);
	console.log("response.purchaseBalancePaidRaw = " + response.purchaseBalancePaidRaw);
	console.log("response.purchaseBilled = " + response.purchaseBilled);
	console.log("response.purchaseBilledRaw = " + response.purchaseBilledRaw);
	console.log("response.redirectUrl = " + response.redirectUrl);
	console.log("response.error = " + response.error);
	console.log("response.subscriberBalance = " + response.subscriberBalance);
	console.log("response.subscriberBalanceRaw = " + response.subscriberBalanceRaw);
	console.log("response.subscriberLoyaltyReference = " + response.subscriberLoyaltyReference);
	console.log("response.subscriberDisplayName = " + response.subscriberDisplayName);
	console.log("response.subscriberUserName = " + response.subscriberUserName);
	console.log("response.subscriberEmailAddress = " + response.subscriberEmailAddress);
	console.log("response.redeemedAmount = " + response.redeemedAmount);
	console.log("response.redeemedAmountRaw = " + response.redeemedAmountRaw);
	console.log("response.cardOnFileAllowed = " + response.cardOnFileAllowed);
	console.log("response.supportAdditionalCCData = " + response.supportAdditionalCCData);
}
/*done*/
function selectPlan(planId) {
	$.getJSON($("#selectPlanForm-"+planId).attr('action'), function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#plan-error").html('<p>'+response.error+'</p>');
			return;
		}
		updateDash(response);
		if(response.showTC) {
			$("#plan").hide();
			showTC();
			return;
		}
		if(response.supportSubscriber && !response.authenticated) {
			showPlanSelectFilter();
			return;
		}
		$("#plan").hide();
		if(response.free) {
			submitFreeForm(response);
			return;
		}
		showCC(response);
	});
}
/*done*/
function showPlanSelectFilter() {
	fromSelectPlan = true;
	$("#planSelectFilter").dialog("open");
}
/*done*/
function planSelectToLogin() {
	$("#planSelectFilter").dialog("close");
	showLoginDialog();
}
/*done*/
function planSelectToSignup() {
	$("#planSelectFilter").dialog("close");
	showSignupDialog();
}
/*done*/
function planSelectToGuest() {
	$.getJSON($("#guestForm").attr('action'), function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#plan-error").html('<p>'+response.error+'</p>');
			$("#planSelectFilter").dialog("close");
			$("#tc").hide();
			$("#plan").fadeIn();
			return;
		}
		updateDash(response);
		fromSelectPlan = false;
		$("#planSelectFilter").dialog("close");
		$("#language-bar").hide();
		$("#plan").hide();
		$("#tc").hide();
		if(response.free) {
			submitFreeForm(response);
			return;
		}
		if(response.promoCode.length > 0) {
			submitCommitPromoForm();
		} else {
			showCC(response);
		}
	});
}
/*done*/
function showRedeemPINAuth() {
	fromRedeemPIN = true;
	$("#redeemPIN-auth").dialog("open");
}
/*done*/
function showRedeemPINStatus() {
	$("#redeemPIN-status").dialog("open");
}
/*done*/
function hideRedeemPINStatus() {
	$("#redeemPIN-status").dialog("close");
}
/*done*/
function redeemToLogin() {
	$("#redeemPIN-auth").dialog("close");
	showLoginDialog();
}
/*done*/
function redeemToSignup() {
	$("#redeemPIN-auth").dialog("close");
	showSignupDialog();
}
/*done*/
function showLoginDialog() {
	$("#forgetpassword").hide();
	$("#resetpassword").hide();
	$("#dialog-login").dialog("open");
	showLogin();
}
/*done*/
function showSignupDialog(action) {
	$("#dialog-signup").dialog("open");
	showSignup();
}
/*done*/
function hideLoginDialog() {
	$("#dialog-login").dialog("close");
	fromSelectPlan = false;
	fromRedeemPIN = false;
}
/*done*/
function hideSignupDialog() {
	$("#dialog-signup").dialog("close");
	fromSelectPlan = false;
	fromRedeemPIN = false;
}
/*done*/
function changePlan(planId) {
	$.getJSON($("#changePlanForm-"+planId).attr('action'), function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#changeplan-error").html('<p>'+response.error+'</p>');
			return;
		}
		updateDash(response);
		$("#changeplan").hide();
		if(response.free) {
			submitFreeForm(response);
			return;
		}
		showCC(response);
	});
}
/*done*/
function submitPINForm() {
	$.getJSON($("#guestForm").attr('action'), function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#redeemPIN-status-error").html('<p>'+response.error+'</p>');
			$("#redeemPIN-status-error").css({'display': 'inline'});
			$("#redeemPIN-status-success").hide();
			$("#redeemPIN-status-button-close").hide();
	    	$("#redeemPIN-status-button-tryagain").css({'display': 'table-cell'});
			showRedeemPINStatus();
			return;
		}
		if(!response.authenticated) {
			showRedeemPINAuth();
			return;
		}
		var params = $("#redeemPINForm").serializeArray();
		$.getJSON($("#redeemPINForm").attr('action'), params, function(response, status, request){
			//debugResponse(response);
			if(response.error.length > 0) {
				$("#redeemPIN-status-error").html('<p>'+response.error+'</p>');
				$("#redeemPIN-status-error").css({'display': 'inline'});
				$("#redeemPIN-status-success").hide();
				$("#redeemPIN-status-button-close").hide();
		    	$("#redeemPIN-status-button-tryagain").css({'display': 'table-cell'});
				showRedeemPINStatus();
				return;
			}
			updateDash(response);
			if(onCC) {
				showCC(response);
			}
			$("#redeemPIN-status-error").hide();
			$("#redeemPIN-status-amount").text(response.redeemedAmount);
			$("#redeemPIN-status-success").css({'display': 'inline'});
			$("#redeemPIN-status-button-close").css({'display': 'table-cell'});
	    	$("#redeemPIN-status-button-tryagain").hide();
			showRedeemPINStatus();
		});
	});
}
/*done*/
function submitPromoForm() {
	var params = $("#redeemPromoForm").serializeArray();
	$.getJSON($("#redeemPromoForm").attr('action'), params, function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#plan-error").html('<p>'+response.error+'</p>');
			return;
		}
		updateDash(response);
		if(response.showTC) {
			$("#plan").hide();
			showTC();
			return;
		}
		if(response.supportSubscriber && !response.authenticated) {
			showPlanSelectFilter();
			return;
		}
		$("#plan").hide();
		submitCommitPromoForm();
	});
}
/*done*/
function submitChangePromoForm() {
	var params = $("#changePromoForm").serializeArray();
	$.getJSON($("#changePromoForm").attr('action'), params, function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#changeplan-error").html('<p>'+response.error+'</p>');
			return;
		}
		updateDash(response);
		$("#changeplan").hide();
		submitCommitPromoForm();
	});
}
/*done*/
function showPlan() {
	$("#plan-error").html('<p></p>');
	$("#plan").fadeIn();
}
/*done*/
function showTC() {
	$("#tc-error").html('<p></p>');
	if($("#tc-checkbox").is(':checked')) {
		$("#tc-buttons a:first").button("enable");
	} else {
		$("#tc-buttons a:first").button("disable");
	}
	$("#tc").fadeIn();
}
/*done*/
function tcContinue() {
	$.getJSON($("#tcForm").attr('action'), function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#tc-error").html('<p>'+response.error+'</p>');
			return;
		}
		updateDash(response);
		if(response.supportSubscriber && !response.authenticated) {
			showPlanSelectFilter();
			return;
		}
		$("#tc").hide();
		if(response.free) {
			submitFreeForm(response);
			return;
		}
		if(response.promoCode.length > 0) {
			submitCommitPromoForm();
		} else {
			showCC(response);
		}
	});
}
/*done*/
function tcCancel() {
	$("#tc").hide();
	showPlan();
}
/*done*/
function showLogin() {
	$("#login-error").html('<p></p>');
	$("#loginForm").validate().resetForm();
	$("#loginForm input").val('');
	$("#loginForm input").removeClass("error");
	$("#login .error-icon").css({'display': 'none'});
	$("#login").fadeIn();
}
/*done*/
function submitLoginForm() {
	var params = $("#loginForm").serializeArray();
	$.getJSON($("#loginForm").attr('action'), params, function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#login-error").html('<p>'+response.error+'</p>');
			return;
		}
		updateDash(response);
		if(response.authenticated) {
			$("#dialog-login").dialog("close");
			if(fromSelectPlan) {
				fromSelectPlan = false;
				$("#plan").hide();
				$("#tc").hide();
				$("#language-bar").hide();
				if(response.free) {
					submitFreeForm(response);
					return;
				}
				if(response.promoCode.length > 0) {
					submitCommitPromoForm();
				} else {
					showCC(response);
				}
				return;
			}
			if(fromRedeemPIN) {
				fromRedeemPIN = false;
				submitPINForm();
				return;
			}
			if(onCC) {
				showCC(response);
			}
		}
	});
}
/*done*/
function showSignup() {
	$("#signup-error").html('<p></p>');
	$("#signupForm").validate().resetForm();
	$("#signupForm input").val('');
	$("#signupForm input").removeClass("error");
	$("#signup .error-icon").css({'display': 'none'});
	$("#signup").fadeIn();
}
/*done*/
function submitSignupForm() {
	var params = $("#signupForm").serializeArray();
	$.getJSON($("#signupForm").attr('action'), params, function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#signup-error").html('<p>'+response.error+'</p>');
			return;
		}
		updateDash(response);
		$("#dialog-signup").dialog("close");
		if(fromSelectPlan) {
			fromSelectPlan = false;
			$("#plan").hide();
			$("#tc").hide();
			$("#language-bar").hide();
			if(response.free) {
				submitFreeForm(response);
				return;
			}
			if(response.promoCode.length > 0) {
				submitCommitPromoForm();
			} else {
				showCC(response);
			}
			return;
		}
		if(fromRedeemPIN) {
			fromRedeemPIN = false;
			submitPINForm();
			return;
		}
		if(onCC) {
			showCC(response);
		}
	});
}
/*done*/
function showForgetpassword() {
	$("#forgetpassword-error").html('<p></p>');
	$("#forgetpasswordForm").validate().resetForm();
	$("#forgetpasswordForm input").val('');
	$("#forgetpasswordForm input").removeClass("error");
	$("#forgetpassword .error-icon").css({'display': 'none'});
	$("#forgetpassword").fadeIn();
}
/*done*/
function submitForgetpasswordForm() {
	var params = $("#forgetpasswordForm").serializeArray();
	$.getJSON($("#forgetpasswordForm").attr('action'), params, function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#forgetpassword-error").html('<p>'+response.error+'</p>');
			return;
		}
		if(response.securityQuestionId.length > 0 && response.securityQuestionText.length >0) {
			$("#resetpasswordSecurityQuestionId").val(response.securityQuestionId);
			$("#resetpasswordUserId").val($("#forgetpasswordUserId").val());
			$("#resetpassword-table td:first label").text(response.securityQuestionText);
			$("#forgetpassword").hide();
			showResetpassword();
		}
	});
}
/*done*/
function showResetpassword() {
	$("#resetpassword-error").html('<p></p>');
	$("#resetpasswordForm").validate().resetForm();
	$("#securityAnswer").val('');
	$("#passwordNew").val('');
	$("#passwordNewConfirm").val('');
	$("#resetpasswordForm input").removeClass("error");
	$("#resetpassword .error-icon").css({'display': 'none'});
	$("#resetpassword").fadeIn();
}
/*done*/
function submitResetpasswordForm() {
	var params = $("#resetpasswordForm").serializeArray();
	$.getJSON($("#resetpasswordForm").attr('action'), params, function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#resetpassword-error").html('<p>'+response.error+'</p>');
			return;
		}
		$("#resetpassword").hide();
		showLogin();
	});
}
/*done*/
function showCC(response) {
	$("#cc-error").html('<p></p>');
	if(response != null) {
		$("#cc-amount").text(response.due);
		if(response.stillOwe) {
			$("#cc-stillowe").css({'display': 'inline'});
		} else {
			$("#cc-stillowe").hide();
		}
		if(response.supportAdditionalCCData) {
			$("#add-cc-table").css({'display': 'inline'});
		}
		else {
			$("#add-cc-table").hide();
		}
		if(response.authenticated) {
			if(response.cardOnFileAllowed) {
				$("#cc-table-tostore").css({'display': 'table-row'});
			} else {
				$("#cc-table-tostore").hide();
			}
			if(response.supportLoyalty) {
				$("#purchaseLoyaltyReference").attr('value', response.subscriberLoyaltyReference);
				$("#cc-loyalty").css({'display': 'inline'});
			} else {
				$("#cc-loyalty").hide();
			}
		}
		if((response.mopcode.length > 0 && response.moptype.length > 0 && response.cardOnFileAllowed) || (response.allowPayByBalance) || (response.allowBillto)) {
			if(response.mopcode.length > 0 && response.moptype.length > 0 && response.cardOnFileAllowed) {
				$("#mop-yes").val(response.mopcode);
				$("#mop-type").text(response.moptype);
				$("#mop-code").text(response.mopcode);
				$("#mop-yes-tr").css({'display': 'table-row'});
			}
			if(response.allowPayByBalance) {
				$("#mop-balance-tr").css({'display': 'table-row'});
				$("#mop-balance-value").text(response.subscriberBalance);
			} else {
				$("#mop-balance").removeAttr('checked');
				$("#mop-balance-tr").hide();
			}
			$("#cc-mop").css({'display': 'inline'});
		} else {
			$("#mop-balance").removeAttr('checked');
			$("#cc-mop").hide();
		}
	}
	if($("#mop-yes").is(':checked')) {
		$("#cc-table input").attr('disabled', 'disabled');
		$("#add-cc-table input").attr('disabled', 'disabled');
		$("#add-cc-table select").attr('disabled', 'disabled');
	}
	else if($("#mop-balance").is(':checked')) {
		$("#cc-table input").attr('disabled', 'disabled');
		$("#add-cc-table input").attr('disabled', 'disabled');
		$("#add-cc-table select").attr('disabled', 'disabled');
	}
	else if($("#mop-bill").is(':checked')) {
		$("#cc-table input").attr('disabled', 'disabled');
		$("#add-cc-table input").attr('disabled', 'disabled');
		$("#add-cc-table select").attr('disabled', 'disabled');
	}
	else {
		$("#cc-table input").removeAttr('disabled');
		$("#add-cc-table input").removeAttr('disabled');
		$("#add-cc-table select").removeAttr('disabled');
	}
	$("#ccForm").validate().resetForm();
	$("#ccForm input").removeClass("error");
	$("#cc .error-icon").css({'display': 'none'});
	onCC = true;
	$("#cc").fadeIn();
}
/*done*/
function submitFreeForm() {
	$.getJSON($("#freeForm").attr('action'), function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#plan-error").html('<p>'+response.error+'</p>');
			$("#plan").fadeIn();
			return;
		}
		updateDash(response);
		showSummary(response);
	});
}
/*done*/
function showSummary(response) {
	$("#summary-table td:eq(0)").html(response.selectedPlanName);
	$("#summary-table td:eq(1)").html(response.selectedPlanPrice);
	$("#summary-table td:eq(2)").html(response.selectedPlanDesc);
	$("#summary-table td:eq(3)").text(response.selectedPlanLoyaltyPoints);
	if(response.purchaseCCAmountRaw > 0 && response.purchaseBalancePaidRaw > 0) {
		$("#summary-ccb-balance-amount").text(response.purchaseBalancePaid);
		$("#summary-ccb-cc-amount").text(response.purchaseCCAmount);
		$("#summary-ccb-last4").text(response.purchaseCCLast4);
		$("#summary-ccb-auth").text(response.purchaseCCAuth);
		$("#summary-ccb").css({'display': 'inline'});
		$("#summary-cc").hide();
		$("#summary-bb").hide();
		$("#summary-balance").hide();
		$("#summary-bill").hide();
	} if(response.purchaseBilledRaw > 0 && response.purchaseBalancePaidRaw > 0) {
		$("#summary-bb-balance-amount").text(response.purchaseBalancePaid);
		$("#summary-bb-bill-amount").text(response.purchaseBilled);
		$("#summary-bb").css({'display': 'inline'});
		$("#summary-cc").hide();
		$("#summary-ccb").hide();
		$("#summary-balance").hide();
		$("#summary-bill").hide();
	} else if(response.purchaseCCAmountRaw > 0) {
		$("#summary-cc-last4").text(response.purchaseCCLast4);
		$("#summary-cc-amount").text(response.purchaseCCAmount);
		$("#summary-cc-auth").text(response.purchaseCCAuth);
		$("#summary-cc").css({'display': 'inline'});
		$("#summary-ccb").hide();
		$("#summary-bb").hide();
		$("#summary-balance").hide();
		$("#summary-bill").hide();
	} else if(response.purchaseBalancePaidRaw > 0) {
		$("#summary-balance-amount").text(response.purchaseBalancePaid);
		$("#summary-balance").css({'display': 'inline'});
		$("#summary-cc").hide();
		$("#summary-ccb").hide();
		$("#summary-bill").hide();
		$("#summary-bb").hide();
	} else if(response.purchaseBilledRaw > 0) {
		$("#summary-bill").css({'display': 'inline'});
		$("#summary-cc").hide();
		$("#summary-ccb").hide();
		$("#summary-balance").hide();
		$("#summary-bb").hide();
	}
	if(response.supportDataMeter && response.newTC) {
    	$("#summary-finish").hide();
    	$("#summary-button-finish").hide();
    	$("#summary-button-continue").css({'display': 'table-cell'});
    } else {
    	$("#summary-finish").css({'display': 'inline'});
    	$("#summary-button-finish").css({'display': 'table-cell'});
    	$("#summary-button-continue").hide();
    }
	if(response.newSignup) {
		$("#summary-signup-username").html(response.subscriberUserName);
		$("#summary-signup-email").text(response.subscriberEmailAddress);
		$("#summary-signup").css({'display': 'inline'});
	} else {
		$("#summary-signup").hide();
	}
    if(response.authenticated) {
		if(response.supportLoyalty && (response.subscriberLoyaltyReference != null) && (response.subscriberLoyaltyReference.length > 0) && (response.selectedPlanLoyaltyPoints > 0) ) {
			$("#summary-loyalty-placeholder").text(response.selectedPlanLoyaltyPoints);
			$("#summary-loyalty").css({'display': 'inline'});
		} else {
			$("#summary-loyalty").hide();
		}
    } else {
    	$("#summary-loyalty").hide();
    }
	$("#summary").fadeIn();
	updateDataUsage();
}
/*done*/
function submitCommitPromoForm() {
	$.getJSON($("#commitPromoForm").attr('action'), function(response, status, request){
		showPromo(response);
	});
}
/*done*/
function showPromo(response) {
	//debugResponse(response);
	if(response.error.length > 0) {
		$("#plan-error").html('<p>'+response.error+'</p>');
		$("#plan").fadeIn();
		return;
	}
	updateDash(response);
	$("#promo-table td:eq(0)").html(response.selectedPlanName);
	$("#promo-table td:eq(1)").html(response.selectedPlanPrice);
	$("#promo-table td:eq(2)").html(response.selectedPlanDesc);
	$("#promo-table td:eq(3)").text(response.selectedPlanLoyaltyPoints);
	$("#promo-code").text(response.promoCode);
	if(response.supportDataMeter && response.newTC) {
    	$("#promo-finish").hide();
    	$("#promo-button-finish").hide();
    	$("#promo-button-continue").css({'display': 'table-cell'});
    } else {
    	$("#promo-finish").css({'display': 'inline'});
    	$("#promo-button-finish").css({'display': 'table-cell'});
    	$("#promo-button-continue").hide();
    }
	if(response.newSignup) {
		$("#promo-signup-username").html(response.subscriberUserName);
		$("#promo-signup-email").text(response.subscriberEmailAddress);
		$("#promo-signup").css({'display': 'inline'});
	} else {
		$("#promo-signup").hide();
	}
	if(response.authenticated) {
		if(response.supportLoyalty && (response.subscriberLoyaltyReference != null) && (response.subscriberLoyaltyReference.length > 0) && (response.selectedPlanLoyaltyPoints > 0) ) {
			$("#promo-loyalty-placeholder").text(response.selectedPlanLoyaltyPoints);
			$("#promo-loyalty").css({'display': 'inline'});
		} else {
			$("#promo-loyalty").hide();
		}
    } else {
    	$("#promo-loyalty").hide();
    }
	$("#summary-promo").fadeIn();
	updateDataUsage();
}
/*done*/
function showDatameter() {
	$("#datameter").fadeIn();
}
/*done*/
function showDatameterDialog() {
	$("#dialog-datameter-content").show();
	$("#dialog-datameter-finish").hide();
	$("#dialog-datameter").dialog("open");
}
/*done*/
function hideDatameterDialog() {
	$("#dialog-datameter").dialog("close");
}
/*done*/
function dialogDmWindows() {
	$("#dialog-datameter-content").hide();
	$("#dialog-datameter-finish").fadeIn();
	window.open($("#dmWinForm").attr('action'),'','');
	// To handle logging to Qualution
	$.getJSON($("#logDatameterDownloadWindowsForm").attr('action'));
}
/*done*/
function dialogDmMac() {
	$("#dialog-datameter-content").hide();
	$("#dialog-datameter-finish").fadeIn();
	window.open($("#dmMacForm").attr('action'),'','');
	// To handle logging to Qualution
	$.getJSON($("#logDatameterDownloadMacForm").attr('action'));
}
/*done*/
function dmWindows() {
	$("#datameter").hide();
	$("#datameter-finish").fadeIn();
	window.open($("#dmWinForm").attr('action'),'','');
}
/*done*/
function dmMac() {
	$("#datameter").hide();
	$("#datameter-finish").fadeIn();
	window.open($("#dmMacForm").attr('action'),'','');
}
/*done*/
function summaryToDatameter() {
	$("#summary").hide();
	showDatameter();
}
/*done*/
function promoToDatameter() {
	$("#summary-promo").hide();
	showDatameter();
}
/*done*/
function confirmPayment() {
	$.getJSON($("#guestForm").attr('action'), function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			// TODO display error
			return;
		}
		if($("#mop-yes").is(':checked')) {
			if(response.usedBalance) {
				$("#payment-confirm-balance-insufficient-balance").text(response.subscriberBalance);
				$("#payment-confirm-balance-insufficient-amount").text(response.purchasePriceWithTax);
				$("#payment-confirm-balance-insufficient-due").text(response.due);
				$("#payment-confirm-balance-insufficient-last4").text(response.mopcode);
				$("#payment-confirm-balance-insufficient").css({'display': 'inline'});
				$("#payment-confirm-balance-insufficient-bill").hide();
				$("#payment-confirm-bill").hide();
				$("#payment-confirm-balance-sufficient").hide();
				$("#payment-confirm-cc").hide();
			} else {
				$("#payment-confirm-cc-last4").text(response.mopcode);
				$("#payment-confirm-cc-amount").text(response.purchasePriceWithTax);
				$("#payment-confirm-cc").css({'display': 'inline'});
				$("#payment-confirm-bill").hide();
				$("#payment-confirm-balance-sufficient").hide();
				$("#payment-confirm-balance-insufficient").hide();
				$("#payment-confirm-balance-insufficient-bill").hide();
			}
		}
		else if($("#mop-no").is(':checked')) {
			var last4 = $("#creditCardNumber").val().substring($("#creditCardNumber").val().length - 4);
			if(response.usedBalance) {
				$("#payment-confirm-balance-insufficient-balance").text(response.subscriberBalance);
				$("#payment-confirm-balance-insufficient-amount").text(response.purchasePriceWithTax);
				$("#payment-confirm-balance-insufficient-due").text(response.due);
				$("#payment-confirm-balance-insufficient-last4").text(last4);
				$("#payment-confirm-balance-insufficient").css({'display': 'inline'});
				$("#payment-confirm-balance-insufficient-bill").hide();
				$("#payment-confirm-bill").hide();
				$("#payment-confirm-balance-sufficient").hide();
				$("#payment-confirm-cc").hide();
			} else {
				$("#payment-confirm-cc-last4").text(last4);
				$("#payment-confirm-cc-amount").text(response.purchasePriceWithTax);
				$("#payment-confirm-cc").css({'display': 'inline'});
				$("#payment-confirm-bill").hide();
				$("#payment-confirm-balance-sufficient").hide();
				$("#payment-confirm-balance-insufficient").hide();
				$("#payment-confirm-balance-insufficient-bill").hide();
			}
		}
		else if($("#mop-balance").is(':checked')) {
			$("#payment-confirm-balance-sufficient-amount").text(response.purchasePriceWithTax);
			$("#payment-confirm-balance-sufficient").css({'display': 'inline'});
			$("#payment-confirm-cc").hide();
			$("#payment-confirm-bill").hide();
			$("#payment-confirm-balance-insufficient").hide();
			$("#payment-confirm-balance-insufficient-bill").hide();
		}
		else if($("#mop-bill").is(':checked')) {
			if(response.usedBalance) {
				$("#payment-confirm-balance-insufficient-bill-balance").text(response.subscriberBalance);
				$("#payment-confirm-balance-insufficient-bill-amount").text(response.purchasePriceWithTax);
				$("#payment-confirm-balance-insufficient-bill-due").text(response.due);
				$("#payment-confirm-balance-insufficient-bill").css({'display': 'inline'});
				$("#payment-confirm-balance-insufficient").hide();
				$("#payment-confirm-bill").hide();
				$("#payment-confirm-balance-sufficient").hide();
				$("#payment-confirm-cc").hide();
			} else {
				$("#payment-confirm-bill-amount").text(response.purchasePriceWithTax);
				$("#payment-confirm-bill").css({'display': 'inline'});
				$("#payment-confirm-balance-sufficient").hide();
				$("#payment-confirm-cc").hide();
				$("#payment-confirm-balance-insufficient").hide();
				$("#payment-confirm-balance-insufficient-bill").hide();
			}
		}
		else {
			var last4 = $("#creditCardNumber").val().substring($("#creditCardNumber").val().length - 4);
			if(response.usedBalance) {
				$("#payment-confirm-balance-insufficient-balance").text(response.subscriberBalance);
				$("#payment-confirm-balance-insufficient-amount").text(response.purchasePriceWithTax);
				$("#payment-confirm-balance-insufficient-due").text(response.due);
				$("#payment-confirm-balance-insufficient-last4").text(last4);
				$("#payment-confirm-balance-insufficient").css({'display': 'inline'});
				$("#payment-confirm-balance-insufficient-bill").hide();
				$("#payment-confirm-bill").hide();
				$("#payment-confirm-balance-sufficient").hide();
				$("#payment-confirm-cc").hide();
			} else {
				$("#payment-confirm-cc-last4").text(last4);
				$("#payment-confirm-cc-amount").text(response.purchasePriceWithTax);
				$("#payment-confirm-cc").css({'display': 'inline'});
				$("#payment-confirm-bill").hide();
				$("#payment-confirm-balance-sufficient").hide();
				$("#payment-confirm-balance-insufficient").hide();
				$("#payment-confirm-balance-insufficient-bill").hide();
			}
		}
		$("#payment-confirm").dialog("open");
	});
}
/*done*/
function cancelPayment() {
	$("#payment-confirm").dialog("close");
}
/*done*/
function submitCCForm() {
	$("#payment-confirm").dialog("close");
	var params = $("#ccForm").serializeArray();
	$.getJSON($("#ccForm").attr('action'), params, function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#cc-error").html('<p>'+response.error+'</p>');
			return;
		}
		updateDash(response);
		if(response.stillOwe) {
			showCC(response);
			return;
		}
		$("#cc").hide();
		onCC = false;
		showSummary(response);
	});
}
/*done*/
function ccToChangeplan() {
	$("#cc").hide();
	onCC = false;
	showChangeplan();
}
/*done*/
function showChangeplan() {
	$("#changeplan-error").html('<p></p>');
	$("#changeplan").fadeIn();
}

/*
function loginToGuest() {
	$.getJSON($("#guestForm").attr('action'), function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#login-error").html('<p>'+response.error+'</p>');
			return;
		}
		$("#login").hide();
		if(response.promoCode.length > 0) {
			submitCommitPromoForm();
		} else {
			showCC(response);
		}
	});
}

function loginToSignup() {
	$("#login").hide();
	showSignup();
}*/
/*done*/
function forgetPassword() {
	$("#login").hide();
	showForgetpassword();
}
/*done*/
function forgetpasswordToLogin() {
	$("#forgetpassword").hide();
	showLogin();
}

/*
function forgetpasswordToGuest() {
	$.getJSON($("#guestForm").attr('action'), function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#forgetpassword-error").html('<p>'+response.error+'</p>');
			return;
		}
		$("#forgetpassword").hide();
		if(response.promoCode.length > 0) {
			submitCommitPromoForm();
		} else {
			showCC(response);
		}
	});
}
*/
/*done*/
function resetpasswordToLogin() {
	$("#resetpassword").hide();
	showLogin();
}

/*
function resetpasswordToGuest() {
	$.getJSON($("#guestForm").attr('action'), function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#resetpassword-error").html('<p>'+response.error+'</p>');
			return;
		}
		$("#resetpassword").hide();
		if(response.promoCode.length > 0) {
			submitCommitPromoForm();
		} else {
			showCC(response);
		}
	});
}

function signupToGuest() {
	$.getJSON($("#guestForm").attr('action'), function(response, status, request){
		//debugResponse(response);
		if(response.error.length > 0) {
			$("#signup-error").html('<p>'+response.error+'</p>');
			return;
		}
		$("#signup").hide();
		if(response.promoCode.length > 0) {
			submitCommitPromoForm();
		} else {
			showCC(response);
		}
	});
}

function signupToLogin() {
	$("#signup").hide();
	showLogin();
}*/
/*done*/
function toggleMop(event) {
	if($("#mop-yes").is(':checked')) {
		$("#cc-table input").attr('disabled', 'disabled');
		$("#add-cc-table input").attr('disabled', 'disabled');
		$("#add-cc-table select").attr('disabled', 'disabled');
		$("#ccForm").validate().resetForm();
		$("#ccForm input").removeClass("error");
		$("#cc .error-icon").css({'display': 'none'});
	}
	if($("#mop-balance").is(':checked')) {
		$("#cc-table input").attr('disabled', 'disabled');
		$("#add-cc-table input").attr('disabled', 'disabled');
		$("#add-cc-table select").attr('disabled', 'disabled');
		$("#ccForm").validate().resetForm();
		$("#ccForm input").removeClass("error");
		$("#cc .error-icon").css({'display': 'none'});
	}
	if($("#mop-bill").is(':checked')) {
		$("#cc-table input").attr('disabled', 'disabled');
		$("#add-cc-table input").attr('disabled', 'disabled');
		$("#add-cc-table select").attr('disabled', 'disabled');
		$("#ccForm").validate().resetForm();
		$("#ccForm input").removeClass("error");
		$("#cc .error-icon").css({'display': 'none'});
	}
	if($("#mop-no").is(':checked')) {
		$("#cc-table input").removeAttr('disabled');
		$("#add-cc-table input").removeAttr('disabled');
		$("#add-cc-table select").removeAttr('disabled');
	}
}
/*done*/
function finish() {
	$.getJSON($("#finishForm").attr('action'), function(response, status, request){
		if(response.error.length > 0) {
			// TODO
			return;
		}
		window.location.replace(response.redirectUrl);
	});
}
/*done*/
function changeLanguage(code) {
	window.location.replace($("#languageForm").attr('action') + "&language=" + code);
}
/*done*/
function showNoAddOn() {
	$("#dialog-addon-declined").dialog("open");
}
/*done*/
function showProgress() {
	$("#overlay").dialog("open");
	//$("#progress").css({'display': 'table'});
}
/*done*/
function hideProgress() {
	$("#overlay").dialog("close");
	//$("#progress").css({'display': 'none'});
}
/*done*/
function showBalanceElement() {
	$("#menu-el-to-change").attr('class', 'topmenu');
	$("#balance-el").show();
}
/*done*/
function updateDash(response) {
	if(response.authenticated) {
		$("#balance-item").show("slow");
		$("#balanceHolder").html(response.subscriberBalance);
		$("#loginAndNameTextToggle").attr('class', 'nohover');
		$("#username-toggle-text").parent().css({"max-width": 98, "overflow": "hidden", "white-space": "normal"});
		$("#username-toggle-text").html("Hello, " + response.subscriberDisplayName);
		$("#cred-ddl").remove();
	}
}
/*done*/
function setFooterLeft(offset) {
	$(".footer").css({"left": $(".ui-dialog").offset().left + offset + "px"});
}
/*done*/
function handleBookmark() {
	  $(".page-bookmark").click(function(e){
		    e.preventDefault(); // this will prevent the anchor tag from going the user off to the link
		    var bookmarkUrl = this.href;
		    var bookmarkTitle = this.title;

		    if(navigator.userAgent.toLowerCase().indexOf('chrome') > -1) {
		    	alert('Your browser does not support this bookmark action');
		         return false;
		    } else if (window.sidebar) { // For Mozilla Firefox Bookmark
		        window.sidebar.addPanel(bookmarkTitle, bookmarkUrl,"");
		    } else if( window.external || document.all) { // For IE Favorite
		    	try{		    		
		    		window.external.AddFavorite( bookmarkUrl, bookmarkTitle);
		    	}catch(err) {
		    		// AddFavorite not supported in Chrome
		    	}
		    } else if(window.opera) { // For Opera Browsers
		        $("a#page-bookmark").attr("href",bookmarkUrl);
		        $("a#page-bookmark").attr("title",bookmarkTitle);
		        $("a#page-bookmark").attr("rel","sidebar");
		    } else { // for other browsers which does not support
		         alert('Your browser does not support this bookmark action');
		         return false;
		    }
		  });
}
/*done*/
function updateDataUsage() {
	isUpdateDataUsage = true;
	console.log("!!!!!!!!!!Start updating .footer");
	$.getJSON($("#usageForm").attr("action"), function(response) {
		console.log("response.active = " + JSON.stringify(response.active));
		console.log("response.inactive = " + JSON.stringify(response.inactive));
		console.log("response.plans = " + JSON.stringify(response.plans));
		if((!response.active||response.active.length==0)&&(!response.inactive||response.inactive.length==0)) {
			$("#footer-no-plan").show();
			$("#footer-title").hide();
			$("#footer-plan-name").hide();
			$("#footer-expiration").hide();
			$("#footer-expiration-date").hide();
			$("#footer-additional-purchases").hide();
		} else {
			if(response.active && response.active.length > 0) {
				$.each(response.active, function(index, map) {
					var name = map.planName;
					var expDate = map.expirationDate;
					var includedActiveData = map.includedData.toLowerCase();
					$.each(response.plans, function(index, plan) {
						if(map.sku == plan.sku) {
							name = plan.name;
						}
					});
					if(includedActiveData == 'unlimited') {
						$("#footer-plan-name").html(name + ":UNLIMITED");
						$("#footer-expiration-date").html(expDate);
					}else {						
						$("#footer-plan-name").html(name + ":" + map.remainingData + "/" + map.includedData);
						$("#footer-expiration-date").html(expDate);
					}
				});
				$("#footer-no-plan").hide();
				$("#footer-title").show();
				$("#footer-plan-name").show();
				$("#footer-expiration").show();
				$("#footer-expiration-date").show();
			}
			if(response.inactive && response.inactive.length > 0) {
				$("#footer-additional-purchases-list").empty();
				$.each(response.inactive, function(index, map) {
					var name = map.planName;
					var includedInactiveData = map.includedData.toLowerCase();
					$.each(response.plans, function(index, plan) {
						if(map.sku == plan.sku) {
							name = plan.name;
						}
					});
					if(includedInactiveData == 'unlimited') {
						$("#footer-additional-purchases-list").append(name + ":UNLIMITED"+ "<br />");
					}else {						
						$("#footer-additional-purchases-list").append(name + ":" + map.remainingData + "/" + map.includedData + "<br />");
					}
				});
				$("#footer-additional-purchases").show();
				$("#footer-no-plan").hide();
				$("#footer-title").show();
			}			
		}
		$(".footer").css({"margin-bottom": -($(".footer").outerHeight()) + $("#top-box-content").outerHeight() + ($("#footer-content").outerHeight()-$("#footer-content").innerHeight())/2 + "px"});
		console.log("!!!!!!!!!!Finished updating .footer");
	});
}
/*done*/
var myPopupWindow = '';
/*done*/
function openPopupWindow(url, name, width, height)
{
    //Remove special characters from name
    name = name.replace(/\/|\-|\./gi, "");
 
    //Remove whitespaces from name
    var whitespace = new RegExp("\\s","g");
    name = name.replace(whitespace,"");
 
    //If it is already open
    if (!myPopupWindow.closed && myPopupWindow.location)
    {
        myPopupWindow.location.href = encodeUrl(url);
    }
    else
    {
        myPopupWindow= window.open(encodeUrl(url),name, "location=no, scrollbars=yes, resizable=yes, toolbar=no, menubar=no, width=" + width + ", height=" + height );
        if (!myPopupWindow.opener) myPopupWindow.opener = self;
    }
    scrollTo(($(document).width() - $(window).width()) / 2, 0);
     //If my main window has focus - set it to the popup
    if (window.focus) {myPopupWindow.focus();}
}
/*done*/
function encodeUrl(url)
{
    if (url.indexOf("?")>0)
    {
        encodedParams = "?";
        parts = url.split("?");
        params = parts[1].split("&");
        for(i = 0; i < params.length; i++)
        {
            if (i > 0)
            {
                encodedParams += "&";
            }
            if (params[i].indexOf("=")>0) //Avoid null values
            {
                p = params[i].split("=");
                encodedParams += (p[0] + "=" + escape(encodeURI(p[1])));
            }
            else
            {
                encodedParams += params[i];
            }
        }
        url = parts[0] + encodedParams;
    }
    return url;
}
/*done*/
function showAjaxErrorDialog() {
	$("#dialog-ajax-error").dialog("open");
}
/*done*/
function hideAjaxErrorDialog() {
	$("#dialog-ajax-error").dialog("close");
}
/*done*/
function showDialogContact() {
	$("#dialog-contact").dialog("open");
}
/*done*/
function hideDialogContact() {
	$("#dialog-contact").dialog("close");
}

function updateApplePadOrientation() {
		window.scrollTo(0, 10);
}

function updateOrientation() {

	var vpwidth = 1000;
	var vlwidth = 1500;
	var viewport = document.querySelector("meta[name=viewport]");
	var ua = navigator.userAgent;
	var androidVersion = parseFloat(ua.slice(ua.indexOf("Android")+8));
	
	switch (window.orientation) {
	  case 0: //portrait
		  if(androidVersion > 2.3) {
			  $('#bg').css('top', '-' + screen.height/2 + 'px');
			  $('#bg').css('left', '-' + screen.width/2 + 'px');
			  $('#bg').css('width', '200%');
			  $('#bg').css('height', '200%');
			  $('#bg > img').css('min-height', '100%');
		  }
	  viewport.setAttribute('content', 'width=' + vpwidth + ', initial-scale=0.4, user-scalable=yes;');
	    break;
	  case 90: case -90: //landscape
		  
	//set the viewport attributes to whatever you want!
	  viewport.setAttribute('content', 'width=' + vlwidth + ', initial-scale=0.4, user-scalable=yes;');
	  if(androidVersion > 2.3) {
		  $('#bg').css('top', '-' + screen.height/2 + 'px');
		  $('#bg').css('left', '-' + screen.width/2 + 'px');
		  $('#bg').css('width', '200%');
		  $('#bg').css('height', '200%');
		  $('#bg > img').css('min-height', '100%');
	  }
	    break;
	  default:
		  
	    //set the viewport attributes to whatever you want!
	  viewport.setAttribute('content', 'width=' + vpwidth + ', initial-scale=0.4, user-scalable=yes;');
		  break;
	}
//	alert("Android Version: " + androidVersion);
}
