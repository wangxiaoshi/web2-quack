import {User} from "./User";

export class Comment {
    id: number;
    text: string;
    date: Date;
    author: User;

    static fromObject(object: any) : Comment {
        const comment = new Comment();
        comment.id = object.id;
        comment.text = object.text;
        comment.date = new Date(parseInt(object.date));
        comment.author = User.fromObject(object.author);
        return comment;
    }
}
