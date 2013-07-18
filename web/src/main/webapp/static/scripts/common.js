$(function(e) {

    // remove hash in URLs (facebook login adds a hash for security purposes)
    removeLocationHash(e);

    highlightFormInputs();

});

var highlightFormInputErrors = function(elems) {
    elems.addClass('errorInput');
};

var highlightFormInputs = function() {
    $('form').find('input:text, input:password').each(function() {
        $(this).focus(function() {
            $(this).removeClass('innerShadow').addClass('outerShadow');
        });
        $(this).blur(function() {
            $(this).removeClass('outerShadow').addClass('innerShadow');
        });
    });
};

var registerValidationHandler = function(element, validation, validationMsg, eventTypes) {

    if (!eventTypes) {
        eventTypes = 'blur';
    }

    element.bind(eventTypes, function() {
        if (validation(element)) {
            element.removeClass('errorInput');
            element.next('span.error').remove();
        } else if (!element.hasClass('errorInput')) {
            element.addClass('errorInput');
            var id = element.attr('id') + '.errors';
            var spanElement = $('<span class="error" id="' + id + '">' + validationMsg + '</span>');
            spanElement.insertAfter(element);
        }
    });
};

/**
 * @argument {map} map associateds defaultValue to input element
 */
var registerDefaultValuesListener = function(map) {

    $.each(map, function(defaultValue, inputElement) {

        if (inputElement.val() === '' && defaultValue !== '') {
            inputElement.val(defaultValue);
            inputElement.addClass('defaultValue');
        }

        inputElement.focus(function() {
            if (inputElement.hasClass('defaultValue')) {
                inputElement.setCaret(0);
            }
        });

        inputElement.keydown(function() {
            if (inputElement.hasClass('defaultValue')) {
                inputElement.val('');
                inputElement.removeClass('defaultValue');
            }
        });

        inputElement.blur(function() {
            if (inputElement.val() === '') {
                inputElement.val(defaultValue);
                inputElement.addClass('defaultValue');
            }
        });
    });

};

var removeLocationHash = function(e) {
    if (window.location.hash !== '') {
        window.location.hash = ''; // for older browsers, leaves a # behind
        history.pushState('', document.title, window.location.pathname);
        if (e.hasOwnProperty('preventDefault')) {
            e.preventDefault(); // no page reload
        }
    }
};

jQuery.fn.setCaret = function(pos) {
    var input = this[0];
    if (input.setSelectionRange) {
        input.setSelectionRange(pos, pos);
    } else if (input.createTextRange) {
        var range = input.createTextRange();
        range.collapse(true);
        range.moveEnd('character', pos);
        range.moveStart('character', pos);
        range.select();
    }
};

/*
 * Requires:
 *  <script type="text/javascript" src="http://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
 */
var showRecaptcha = function(element) {
    return Recaptcha.create("6LdtxNoSAAAAAPf0oSxnd02SVtDbTVtyJTdUCnTx", element,
            {
                theme: "white"
            });
};

var scorePassword = function(password) {
    var score = 0;

    if (!password) {
        return score;
    }

    // award every unique letter until 5 repetitions
    var letters = new Object();
    var passwordLength = password.length;
    for (var i = 0; i < passwordLength; i++) {
        letters[password[i]] = (letters[password[i]] || 0) + 1;
        score += 5.0 / letters[password[i]];
    }

    // bonus points for mixing it up
    var variations = {
        digits: /\d/.test(password),
        lower: /[a-z]/.test(password),
        upper: /[A-Z]/.test(password),
        nonWords: /\W/.test(password)
    };

    var variationCount = 0;
    for (var check in variations) {
        variationCount += (variations[check] === true) ? 1 : 0;
    }
    score += (variationCount - 1) * 10;

    return parseInt(score);
};

var checkPasswordStrength = function(password) {
    var result = "Poor";
    var score = scorePassword(password);
    if (score > 80) {
        result = "Strong";
    } else if (score > 60) {
        result = "Good";
    } else if (score > 30) {
        result = "Weak";
    }
    return result;
};

var extractUrls = function(text, maxToExtract) {

    /*
     http(s)?:\/\/                     : the http or https schemes
     [\w-]+(\.[\w-]+)+\.?              : domain name with at least two components; allows a trailing dot
     (:\d+)?                           : the port (optional)
     (\/\S*)?                          : the path (optional)
    */
    var regex = /http(s)?:\/\/[\w-]+(\.[\w-]+)+\.?(:\d+)?(\/\S*)?/;

    var urls = [];
    var tokens = text.split(/\s+/);
    for (var i = 0; i < tokens.length; i++) {
        var token = tokens[i];
        if (token.match(regex)) {
            urls.push(token);
            if (urls.length >= maxToExtract) {
                return urls;
            }
        }
    }

    return urls;
};
