$(function() {

    $('form div.button').click(function() {
        var loginForm =$(this).parent('form');
        loginForm.attr('action', window.location.href);
        loginForm.submit();
    });

    $('#fbLoginError, #basicLoginError').each(function() {
        if($(this).text() === '') {
            $(this).css('display', 'none');
        } else {
            $(this).css('display', 'show');
        }
    });

});


