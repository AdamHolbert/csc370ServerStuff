$(document).ready(function(){
    $(".boldify").on("click", function(){
        console.log($(this).css('font-weight'));
        if($(this).css('font-weight') == "bold"){
            $(this).css({
                "font-weight":"400",
                "font-size":"16px"
            });
        } else {
            $(this).css({
                "font-weight":"bold",
                "font-size":"40px"
            });
        }
    });
});
