const UserProfile = ({gender}) => {
    const imageNumber = Math.floor(Math.random() * (50 - 21) + 21)
    const url = `https://randomuser.me/api/portraits/med/${gender}/${imageNumber}.jpg`;

}

export default UserProfile;

