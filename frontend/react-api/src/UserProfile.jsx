const UserProfile = ({name, gender}) => {
    const imageNumber = Math.floor(Math.random() * (50 - 21) + 21)
    const url = `https://randomuser.me/api/portraits/med/${gender}/${imageNumber}.jpg`;

    return (<div>
        <div>{name}</div>
        <div>{imageNumber}</div>
        <img src={url} alt={"profile picture"}/>
    </div>)
}

export default UserProfile;

