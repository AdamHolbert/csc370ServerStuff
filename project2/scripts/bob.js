var theCanvas=document.getElementById("demo");
var theContext=theCanvas.getContext("2d");


var sales = [2, 3, 4, 5, 10, 8, 3, 10, 12, 23, 18, 30, 35, 42, 57, 40, 70];

var scalar = 6;
var width = 100 * scalar;
var height = 100 *scalar;
var unitSpacing = 10;
var border = 1;


function drawSquare(){
	for(var i = 0; i < sales.length; i++){
		sales[i] *= 4;
	}


	theContext.beginPath();
	theContext.moveTo(width - a, 0 + sales[0]);

	for(var a = 0; a < sales.length; a++){
		theContext.fillStyle = "#90AFC5";
		theContext.fillRect(border, border, width, height);
		theContext.lineTo((a*unitSpacing) * scalar, height + border - (scalar * sales[a]));
		theContext.stroke();

	}
	theContext.lineTo(width +border,height + border);
	theContext.lineTo(border,height + border);

	theContext.stroke();
	theContext.closePath();
	theContext.fillStyle = "#68829E";
	theContext.fill();

};
drawSquare();
