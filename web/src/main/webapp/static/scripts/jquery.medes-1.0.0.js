(function($) {

    var $options;
    var $containerDiv;
    var $description;
    var $searchButton;
    var $mediaUrl;
    var $mediaContainer;

    var methods = {
        init: function(options) {
            
            $containerDiv = $(this);
            
            $options = $.extend({
                "serviceUrl" : "/wps",
                "wpsCssClass" : "",
                "descriptionCssClass" : "",
                "mediaUrlCssClass" : "",
                "webPageSummaryCssClass" : "",
                "descriptionImageMaxWidth" : 300,
                "descriptionImageMaxHeight" : 225,
                "thumbnailMaxWidth" : 100,
                "thumbnailMaxHeight" : 100,
                "defaultDescription" : ""
            }, options); 
                    
            $description = createTextArea();
            $containerDiv.append($description);
            
            // autosize must be applied after element is attached to DOM
            $description.autosize({"append": "\n"});
            
            //methods.setMediaUrl('http://www.google.com');
        
        },
        setMediaUrl: function(url, search) {
            if(!$mediaUrl) {
                initMediaUrl();
            }
            $mediaUrl.attr('value', url);
            if(search) {
                $searchButton.click();
            }
        },
        setDescription: function(description) {
            $description.val(description);
        }
    };

    $.fn.medes = function(method) {
        var medesArguments = arguments;
        return this.each(function() {
          if (methods[method]) {
            return methods[ method ].apply(this, Array.prototype.slice.call(medesArguments, 1));
          } else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, medesArguments);
          } else {
            $.error('Method ' + method + ' does not exist on jquery.medes');
          }
        });
    };

    var createMediaUrl = function(searchFunction) {
        
        var $container = $('<div class="medes medesUrlContainer"></div>');

        var $mediaUrl = $('<input type="text" class="medes medesUrl"></input>');
        $container.append($mediaUrl);
        if($options.mediaUrlCssClass) {
            $mediaUrl.addClass($options.mediaUrlCssClass);
        }

        $searchButton = $('<div class="medes medesSearchButton"></div>');
        $mediaUrl.after($searchButton);
        $searchButton.click(searchFunction);

        return $container;
    };
    
    var createTextArea = function() {
        
        var $description = $('<textarea id="foo" class="medes medesDescription"></textarea>');
        $description.css('height', '75px');
        if($options.descriptionCssClass) {
            $description.addClass($options.descriptionCssClass);
        }
        if($options.defaultDescription) {
            $description.val($options.defaultDescription);
        }
    
        var scanText = function(params) {
            return function() {
                
                if(!params) {
                    params = {};
                }
            
                var text;
                if(params.element) {
                    text = params.element.val();
                } else {
                    text = $(this).val();
                }
                
                if(!$mediaUrl && (!params.regex || (params.regex.test(text)))) {
                    var urls = extractUrls(text);
                    if(urls.length >= 1) {
                        methods.setMediaUrl(urls[0], true);
                    }
                }
            
                previousText = text;
            
            };
        };
    
    
        $description.keyup(scanText({"regex" : /\s$/, "element" : $description})); 
        $description.on("postpaste", scanText()).pasteEvents();
        if($options.defaultDescription) {
            $description.focus(function() {
                var text = $description.val();
                if(text === $options.defaultDescription) {
                    $description.val('');
                }
            }).blur(function() {
                var latestText = $description.val();
                if($.trim(latestText) === '') {
                    $description.val($options.defaultDescription);
                } else if(!$mediaUrl) {
                    scanText({"element" : $description});
                }
            });
        }
        
        return $description;
    };

    var resizeImage = function(img, actualWidth, actualHeight, maxWidth, maxHeight) {
        var ratio = 0; 

        // Check if the current width is larger than the max
        if (actualWidth > maxWidth) {
            ratio = maxWidth / actualWidth;   // get ratio for scaling image
            img.css("width", maxWidth); // Set new width
            img.css("height", actualHeight * ratio);  // Scale height based on ratio
            actualHeight = actualHeight * ratio;    // Reset height to match scaled image
            actualWidth = actualWidth * ratio;    // Reset width to match scaled image
        }

        // Check if current height is larger than max
        if (actualHeight > maxHeight) {
            ratio = maxHeight / actualHeight; // get ratio for scaling image
            img.css("height", maxHeight);   // Set new height
            img.css("width", actualWidth * ratio);    // Scale width based on ratio
            actualWidth = actualWidth * ratio;    // Reset width to match scaled image
        }
    
        if(img.width() === 0) {
            img.css("width", actualWidth);
        }
    
        if(img.height() === 0) {
            img.css("height", actualHeight);
        }
    
    };

    var endsWith = function(str, suffix) {
        return str.indexOf(suffix, str.length - suffix.length) !== -1;
    };

    var initMediaUrl = function() {
        var $mediaUrlContainer = createMediaUrl(function() {
            
            $mediaUrl.removeClass('error');
            
            var url = $mediaUrl.attr('value');
            if($.trim(url) !== '') {
                fetchWebPageSummary(url, processSuccessfulMediaFetch, processFailedMediaFetch);
            }
        
        });
        $mediaUrl = $mediaUrlContainer.children('input');
        $containerDiv.append($mediaUrlContainer);
    };

    var fetchWebPageSummary = function(url, successHandler, errorHandler) {

        addLoadingImage();

        // clear media container
        if($mediaContainer) {
            $mediaContainer.html('');
        }
        
        $.getJSON($options.serviceUrl, {"url": url}, function(data, textStatus, jqXhr) {
            jqXhr.success(function() {
                successHandler(data);
            });
            jqXhr.error(function() {
                errorHandler(data);
            });
            jqXhr.complete(function() {
                removeLoadingImage();
            });
        });

    };

    var processSuccessfulMediaFetch = function(data) {
        
        var $webPageSummary = $containerDiv.children('div.medesWps');
        var $webPageSummaryContents = $webPageSummary.children('div.medesWpsContents');
        
        var webPageSummaryExists = $webPageSummary.length === 1;
        
        if(webPageSummaryExists) {
            $webPageSummaryContents.css('opacity', '0.5');
        }
        
        var $newWebPageSummary = createWebPageSummary(data);
        var $newWebPageSummaryContents = $newWebPageSummary.children('div.medesWpsContents');
        
        $newWebPageSummary.css('border-top', 'none');
        $newWebPageSummaryContents.css('visibility', 'hidden');

        if(webPageSummaryExists) {
            $webPageSummaryContents.fadeOut(250, function() {
                $(this).replaceWith($newWebPageSummaryContents);
            });
        } else {
            $description.after($newWebPageSummary);
            $webPageSummary = $newWebPageSummary;
        }    
    
        $description.css('border-bottom-style', 'dashed');
    
        var timerId = setInterval(function() {
        
            // adjust height of description for ellipsis
            var $wpsDescription = $('div.medesWps div.medesWpsDescription');
            if(!$wpsDescription || !$wpsDescription.position()) {
                return;
            }
            var wpsDescriptionY = $wpsDescription.position().top;
            
            if(!$newWebPageSummaryContents.position()) {
                return;
            }
            var newWpsContentsY = $newWebPageSummaryContents.position().top;
            
            var headerHeight = wpsDescriptionY - newWpsContentsY - 5;
            $wpsDescription.height($newWebPageSummaryContents.height() - headerHeight);
        
            $wpsDescription.dotdotdot();
            $newWebPageSummaryContents.css('visibility', 'visible');
            
            clearInterval(timerId);
        
        }, 100);
        
    };

    var processFailedMediaFetch = function(errMsg) {
        $mediaUrl.addClass('error');
    };

    var createWebPageSummary = function(data) {
                
        var $webPageSummary = $('<div class="medes medesWps"></div>');
        if($options.wpsCssClass) {
            $webPageSummary.addClass($options.wpsCssClass);
        }

        var $webPageSummaryContents = $('<div class="medes medesWpsContents"></div>');
        $webPageSummary.append($webPageSummaryContents);

        // close button
        var $close = $('<div class="medes medesWpsClose"></div>');
        $close.click(function() {
            $(this).closest('.medesWps').remove();
            $description.css('border-bottom-style', 'solid');

        });
        $webPageSummaryContents.append($close);

        // add first video, if one exists
        var imageUri;
        var isVideo; 
        if(data.webPageSummary) {
            if(data.webPageSummary.videos.length > 0) {
                imageUri = data.webPageSummary.videos[0].videoReference.thumbnailUri;
                isVideo = true;
            } else if(data.webPageSummary.images.length > 0) {
                imageUri = data.webPageSummary.images[0].imageReference.imageUri;
                isVideo = false;
            }
        }

        if(!imageUri) {
            $webPageSummaryContents.append('<span class="error">No media found.</span>');    
            return $webPageSummaryContents;
        }

        var $image;
        if(isVideo) {
            $image = $('<img alt="description video thumbnail" class="medes medesWpsVideo" />');
        } else {
            $image = $('<img alt="description image" class="medes medesWpsImage" />');
        }
        $image.attr('src', imageUri);
        
        // temporarily append to body so that IE can get the image dimensions
        $image.css("margin-left", "-9999px");
        $image.css('visibility', 'visible');
        $('body').prepend($image);
        
        $image.load(function() {
            
            var width = this.width;
            var height = this.height;
            resizeImage($(this), width, height, $options.descriptionImageMaxWidth, $options.descriptionImageMaxHeight);
                        
            // remove hack for IE to get image dimensions (must occur after dimensions are accessed)
            $image.remove();
            $image.css('margin-left', '0px');
   
            $webPageSummaryContents.append($image);
            
            // title
            var $title = $('<a class="medes medesWpsTitle"></a>');
            $title.attr('href', data.webPageSummary.url);
            $title.html(data.webPageSummary.title);
            $webPageSummaryContents.append($title);

            // host
            var $host = $('<div class="medes medesWpsHost"></div>');
            var hostStr = data.webPageSummary.host;
            $host.html(hostStr.replace("www.", ""));
            $webPageSummaryContents.append($host);

            // add description
            var $description = $('<div class="medes medesWpsDescription"></div>');
            $description.html(data.webPageSummary.description);
            $webPageSummaryContents.append($description);
   
            // set height
            $webPageSummaryContents.height($options.descriptionImageMaxHeight);
            
        });

        return $webPageSummary;
    };

    var addLoadingImage = function() {
        $searchButton.removeClass('medesSearchButton').addClass('medesLoadingButton');
    };

    var removeLoadingImage = function() {
        $searchButton.removeClass('medesLoadingButton').addClass('medesSearchButton');
    };

})(jQuery);
