var backendHost = 'http://localhost:8080';
var path_checkAllUsersGroup = '/users-health';
var path_users = '/users/';
var activeSymbol = '&#9989;';
var passiveSymbol = '&#10060;';
var loadingSymbol = '&#128260;';

function checkActiveUsers() {

    // поиск всех пользователей
    let getAllUsers = fetch(backendHost + path_users);
    getAllUsers.then(response => {
        response.json().then(userNames => {
            deleteLoadingLogo();
            //рисуем состояние загрузки для каждого элемента
            userNames.forEach(userName => drawLoadingUserBlock(userName));
            //проверяем состояние каждого пользователя
            userNames.forEach(userName => updateUserStatus(userName));

        }).catch(error => {
            deleteLoadingLogo();
            fatalError();
        });
    }).catch(error => {
        deleteLoadingLogo();
        fatalError();
    })


}

function deleteLoadingLogo(){
    document.getElementById('loading').remove();
}

function drawLoadingUserBlock(userName) {
    var lastElement = document.getElementById('mainContainer').lastChild;

    var userDiv = document.createElement('div');
    userDiv.className = 'user';
    userDiv.id = userName;
    userDiv.innerHTML = `<strong>${userName.toUpperCase()}</strong> status: ${loadingSymbol}`;

    lastElement.after(userDiv);
}

function drawUsersBlock(userInfo) {

    var userDiv = document.getElementById(userInfo.name);
    userDiv.innerHTML = `<strong>${userInfo.name.toUpperCase()}</strong>: ${userInfo.ip} status: ${userInfo.isActive ? activeSymbol : passiveSymbol}`

}

function updateUserStatus(userName) {
    fetch(backendHost + path_users + userName)
        .then(response => {
            response.json().then(
                json => {
                    drawUsersBlock(json);
                }
            )
        })
}

function fatalError() {
    var lastElement = document.getElementById('mainContainer').lastChild;

    var userDiv = document.createElement('h2');
    userDiv.className = 'center';
    userDiv.innerHTML = `Service is not available &#9940;`;

    lastElement.after(userDiv);
}


checkActiveUsers();
