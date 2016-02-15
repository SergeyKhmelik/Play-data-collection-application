function showCongrats() {
    var container = $('.container');
    container.empty();
    container.append('<h3 class="page-center text-center">Thank you for submitting your data</h3>');
}

function addRowToTable(messageJson) {
    var tableRowData = "";
    var table = $('#responsesTable');

    table.find('th').each(function () {
        var headerName = $(this).text();
        var dataString = getTableCellFromJSON(messageJson, headerName);

        if (dataString.trim() === "") {
            tableRowData += '<td>N/A</td>';
        } else {
            tableRowData += '<td>' + dataString + '</td>';
        }
    });

    table.find('tbody').append('<tr>' + tableRowData + '</tr>');
}

function getTableCellFromJSON(messageJson, headerName) {
    var result = "";
    if (!messageJson.hasOwnProperty("answers")) return;

    for (var i in messageJson.answers) {
        var answer = messageJson.answers[i];

        if (!answer.hasOwnProperty("field")) return;
        if (answer.field === headerName) {
            if (answer.value) {
                result = answer.value;
                break;
            } else if (answer.options) {
                result = answer.options.join(", ");
                break;
            }
        }
    }
    return result;
}