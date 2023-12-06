const logoutButtons = document.querySelectorAll('#logout-button');
logoutButtons.forEach((button) => {
    button.addEventListener('click', () => {
        fetch(contextPath + 'logout', {
            method: 'POST',
            credentials: 'include'
        }).then(() => {
            location.reload();
        });
    })
})