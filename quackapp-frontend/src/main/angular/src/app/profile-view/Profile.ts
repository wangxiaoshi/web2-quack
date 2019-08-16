import {User} from "../models/User";

export class Profile {
    userId: number;
    username: string;
    role: string;
    pictureUrl: string;
    realName?: string;
    gender?: string;
    birthday?: Date;
    creationDate: Date;
    lastActiveDate?: Date;

    static fromUser(user: User) : Profile {
        const profile = new Profile();

        profile.userId = user.id;
        profile.username = user.username;

        profile.pictureUrl = 'assets/img/user.png';
        profile.realName = user.realName;
        profile.gender = 'männlich';
        profile.birthday = user.birthday;

        profile.creationDate = user.signUpTimestamp;
        profile.lastActiveDate = user.lastActiveTimestamp;

        if (user.admin) {
            profile.role = 'Admin';
        } else if (user.moderator) {
            profile.role = 'Moderator';
        } else {
            profile.role = 'User';
        }

        return profile;
    }
}
