function changePage(delta) {
    const isMaxPage = document.getElementById("pagination-info").dataset.maxPage === "true";

    const url = new URL(window.location.href);
    const params = url.searchParams;

    let currentPage = parseInt(params.get('page') || 0);
    let newPage = currentPage + delta;

    const decBtn = document.querySelector(".employee-card-container__pagination__dec");
    if (newPage < 0) {
        decBtn.style.pointerEvents = "none";
        return;
    } else {
        decBtn.style.pointerEvents = "";
    }
    const incBtn = document.querySelector(".employee-card-container__pagination__inc");
    if (delta > 0 && isMaxPage) {
        incBtn.style.pointerEvents = "none";
        return;
    } else {
        incBtn.style.pointerEvents = "";
    }

    params.set('page', newPage);
    url.search = params.toString();
    window.location.href = url.toString();
}

function addBtn() {

}

