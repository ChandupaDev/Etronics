function signOut() {

    var call = document.getElementById("out");

    call.href = "SignOut";
}

async  function checkIn() {
    const response = await fetch("CheckSignIn");

    if (response.ok) {

        const json = await response.json();

        const response_dto = json.response_dto;

        if (response_dto.success) {

            const user = response_dto.content;

            let st_quick_link = document.getElementById("st-quick-link");

            let st_quick_link_li_1 = document.getElementById("st-quick-link-li-1");
            let st_quick_link_li_2 = document.getElementById("st-quick-link-li-2");

            st_quick_link_li_1.remove();
            st_quick_link_li_2.remove();

            let new_li_tag1 = document.createElement("a");
            let new_li_a_tag1 = document.createElement("a");
            new_li_a_tag1.href = "#";
            new_li_a_tag1.innerHTML = "Welcome, " +user.first_name + " " + user.last_name;
            new_li_tag1.appendChild(new_li_a_tag1);
            st_quick_link.appendChild(new_li_tag1);

        } else {

        }
    } else {

    }

}


