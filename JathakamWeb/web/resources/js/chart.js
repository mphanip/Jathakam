//This software is provided AS IS without any warranty for any kind of use.Author is not liable for any loss for using this software.

var geocoder = new google.maps.Geocoder();
var map;

$(document).ready(function () {
    $('#center').text('Populated by JQuery');
    $("#name").on('mouseover', editInput);

//    console.log("Date time is " + $("[@id=mainForm:datetime]").text);
//    console.log(".datepicker-input is " + $(".datepicker-input"));

    $(".datepicker-input").on('mouseenter', function () {
        $(this).css('cursor', 'pointer').css('background', 'lightgrey');
    });

    $(".datepicker-input").on('mouseout', function () {
        $(this).css('background', 'inherit');
    });

//    
//    $(".datepicker-input").change(function() {
//        console.log("Datepicker value changed to " + $(".datepicker-input").val());
//        $(".datepicker-label").text($(".datepicker-input").val());
//    });
//    
    $(".datepicker-input").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: "dd M yy",
        showOtherMonths: true,
        selectOtherMonths: true,
        yearRange: "1900:" + (new Date()).getFullYear()
    });
});

google.maps.event.addDomListener(window, 'load', function() {
    console.log("Executing Google DomListener...");
    map = PF('geoMap').getMap();
    map.setOptions({streetViewControl:false});
//    google.maps.event.addListener(map, 'click', handlePointClick);
    console.log("Executed Google DomListener");
});

function editInput() {
    var evt = arguments[0]; // First argument is event object
    var nameElement = $('#' + evt.target.id);
    nameElement.editable(editCallback, {
        type: 'text',
        width: '150px'
    });
}

function editCallback(value, settings) {
    console.log(this);
    console.log(value);
    console.log(settings);

    $(this).val(value);

    return(value);
}

function geocode() {
    var placeName = $("#mainForm\\:address").val();
    var zoomVal = parseInt($("#mainForm\\:mapZoom").text());
    console.log("Given address is " + placeName);
    console.log("zoomVal is " + zoomVal);

    geocoder.geocode({'address': placeName}, function (results, status) {
        if (status === google.maps.GeocoderStatus.OK) {
            map.setCenter(results[0].geometry.location);
        }
        else {
            console.log("Could not find the location");
        }
    });

//    PF('geoMap').geocode(placeName); available in 5.1.2
}

function handlePointClick(event) {
    console.log("handlePointClick occured");
    if (navigator.geolocation)
    {
//        browserSupportFlag = true;
        var latlng = event.latLng;

        geocoder.geocode({'latLng': latlng}, function (results, status)
        {
            if (status === google.maps.GeocoderStatus.OK)
            {
//                onPoint(
//                    [{
//                        name: 'address',
//                        value: results[0].formatted_address},
//                        {name: 'lng',
//                            value: event.latLng.lng()
//                        },
//                        {name: 'lat',
//                            value: event.latLng.lat()
//                        }
//                    ]);
                console.log("Clicked address is " + results[0].formatted_address);
                $("#mainForm\\:address").val(results[0].formatted_address);
            }
        });
    }
}

function writeToScreen(message) {
    $("output").html($("output").html() + message + "\n");
}

//$(function() {
//    $(".datepicker-label").click(function() {
//        console.log("Date picker label clicked");
//        $(".datepicker-input").datepicker({
//            changeMonth: true,
//            changeYear: true,
//            dateFormat: "dd M yy",
//            showOtherMonths: true,
//            selectOtherMonths: true,
//            yearRange: "1900:" + (new Date()).getFullYear()
//        });
//        $(".datepicker-input").focus();
//    });
//    
//    $(".datepicker-input").change(function() {
//        console.log("Datepicker value changed to " + $(".datepicker-input").val());
//        $(".datepicker-label").text($(".datepicker-input").val());
//    });
//});

