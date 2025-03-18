import debounce from "lodash.debounce";

export const HeaderGroup = {
    ADMIN : 0,
    BANK_EMPLOYEE : 1,
    MEDICAL_EMPLOYEE : 2
}

export const convertOrganizationType = (type) => {
    switch (type) {
        case 'MEDICAL_INSTITUTION':
            return 'Медицинское учреждение';
        case 'BLOOD_BANK':
            return 'Банк крови';
        default:
            return 'Unknown';
    }
}

export const convertUserRole = (role) => {
    switch (role) {
        case 'ADMIN':
            return 'Администратор';
        case 'BANK_EMPLOYEE':
            return 'Сотрудник банка крови';
        case 'MEDICAL_EMPLOYEE':
            return 'Сотрудник медицинского учреждения';
    }
}

export function formatWorkingHours(hoursFrom, hoursTo, minutesFrom, minutesTo) {
    if (hoursFrom == null || hoursTo == null || minutesFrom == null || minutesTo == null) {
        return "Нет данных";
    }

    const formatTime = (hours, minutes) =>
        `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;

    return `${formatTime(hoursFrom, minutesFrom)} - ${formatTime(hoursTo, minutesTo)}`;
}

let abortController = null;

export const abstractFetching = async (fetchFunc) => {
    if (abortController) {
        abortController.abort();
    }
    abortController = new AbortController();
    fetchFunc(abortController)
}
