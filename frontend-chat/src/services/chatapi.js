import Axios from "axios";

const api = Axios.create({
    baseURL: "/api"
});

const chatAPI = {
    getMessages: (groupId) => {
        console.log('Calling get messages from API ' + groupId);
        return api.get(`messages/${groupId}`);
    },

    sendMessage: (username, text) => {
        let msg = {
            sender: username,
            content: text
        }

        return api.post("/send", msg
            // или добавить в package.json
            // ,
            // "proxy": "http://localhost:8080"
        )
    }
}


export default chatAPI;
