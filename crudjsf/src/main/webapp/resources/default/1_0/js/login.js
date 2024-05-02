function dropDownLogin() {
	var element = document.getElementById("dropdown-content");
	element.classList.toggle('show');
}

window.onclick = function(event) {
console.log('event.target: ' + event.target);
	
  if (!event.target.matches('.dropdown') && !event.target.matches('.dropdown i') && !event.target.matches('.dropdown span')) {
	  console.log('IF');
	  
    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}