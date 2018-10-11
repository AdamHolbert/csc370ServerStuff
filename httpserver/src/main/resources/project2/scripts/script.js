var slideIndex = 0;
carousel();

function carousel() {
    var i;
    var x = document.getElementsByClassName("mySlides");
    console.log(x);
    for (i = 0; i < x.length; i++) {
      x[i].style.display = "none";
      x[i].style.border = "15px solid #1E656D"
    }
    slideIndex++;
    if (slideIndex > x.length) {slideIndex = 1}
    x[slideIndex-1].style.display = "block";
    setTimeout(carousel, 2000); // Change image every 2 seconds
}
