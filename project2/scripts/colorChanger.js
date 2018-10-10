$(document).ready(function(){
    $(".rowHighlight").on("mouseover", function(){
        $(this).css({
            "background-color":"#90AFC5"
        });
    });
    $(".rowHighlight").on("mouseout", function(){
        $(this).css({
            "background-color":"#1E656D"
        });
    });
});
