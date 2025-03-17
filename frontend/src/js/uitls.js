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

export function formatWorkingHours(workingHours) {
    if (!Array.isArray(workingHours) || workingHours.length < 2) {
        return "Нет данных";
    }

    const formatTime = ({ hours, minutes }) =>
        `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;

    return `${formatTime(workingHours[0])} - ${formatTime(workingHours[1])}`;
}
