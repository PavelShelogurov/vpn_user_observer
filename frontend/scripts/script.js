var backendHost = 'http://localhost:8080/health';
var activeSymbol = '&#9989;';
var passiveSymbol = '&#10060;';

function checkActiveUsers() {
    var request = new XMLHttpRequest();

    let response = fetch(backendHost);
    response.then(
        response => {
            response.json().then(
                json => {
                    var arrayLength = json.length;
                    for (var i = 0; i < arrayLength; i++) {
                        drawUsersBlock(json[i]);
                    }

                }
            )

        }
    ).catch(
        error => fatalError()
    )
}

function drawUsersBlock(userInfo) {
    var lastElement = document.getElementById('mainContainer').lastChild;

    var userDiv = document.createElement('div');
    userDiv.className = 'user';
    userDiv.innerHTML = `<strong>${userInfo.name.toUpperCase()}</strong>: ${userInfo.ip} status: ${userInfo.isActive ? activeSymbol : passiveSymbol}`

    lastElement.after(userDiv);
}

function fatalError() {
    var lastElement = document.getElementById('mainContainer').lastChild;

    var userDiv = document.createElement('h2');
    userDiv.className = 'center';
    userDiv.innerHTML = `Service is not available &#9940;`;

    lastElement.after(userDiv);
}


checkActiveUsers();