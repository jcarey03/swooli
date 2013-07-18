$(function() {

    showRecaptcha('recaptchaWidget');

    highlightFormInputErrors($('#accountForm .error').prev('input'));

    // submit button handler
    $('#createAccountButton').click(function() {
        $('#accountForm').submit();
    });

    registerInputsListener();

    registerDefaultValuesListener({
        'First' : $('#firstName'),
        'Last' : $('#lastName')
    });

});

var registerInputsListener = function() {

    // highligh inputs in error when DOM loads


    // register password strength
    $('#password').keyup(function() {
       var password = $(this).val();

       var passwordStrengthElement = $('#passwordStrength');
       passwordStrengthElement.removeClass();

       if(password === '') {
           passwordStrengthElement.text('');
           return;
       }

       var strength = scorePassword(password);
       if(80 < strength) {
           passwordStrengthElement.addClass('passwordStrong');
           passwordStrengthElement.text('Strong');
       } else if(60 < strength) {
           passwordStrengthElement.addClass('passwordGood');
           passwordStrengthElement.text('Good');
       } else if(30 < strength) {
           passwordStrengthElement.addClass('passwordWeak');
           passwordStrengthElement.text('Weak');
       } else {
           passwordStrengthElement.addClass('passwordPoor');
           passwordStrengthElement.text('Poor');
       }
    });

    // validation

    // email check
    registerValidationHandler(
        $('#email'),
        function(element) {
            var re = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
            return re.test(element.val());
        },
        'A valid email is required.'
    );

    // password check
    registerValidationHandler(
        $('#password'),
        function(element) {
            return $.trim(element.val()).length > 0;
        },
        'A password is required.'
    );

    // confirm password check
    registerValidationHandler(
        $('#confirmPassword'),
        function(element) {
            return element.val() === $('#password').val();
        },
        'Passwords must match.'
    );

    // gender check
    registerValidationHandler(
        $('#gender'),
        function(element) {
            return element.find('option:selected').index() > 0;
        },
        'Gender is required.',
        'blur change'
    );
};

