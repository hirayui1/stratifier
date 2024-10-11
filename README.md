Stratifier allows you to view and list the highest contributor across an organization on GitHub. 

Currently, it is quite slow as it takes 139 seconds on Google's 2775 repositories. I will be working on making it faster on dealing with large datasets.

Use this program by simply running it and searching for `localhost:8080/github/org/{org-name}/contributors` on your browser, but if you want to try searching for large scale organizations (such as google), you will have to generate a fine-grained token from your GitHub profile settings. 

You can do this very easily by searching for `github.com/settings/apps` and generating a fine-grained token there. After doing so, you'll have to add the token to your machine's environment variables with the variable being named `GITHUB_TOKEN`. This step will authenticate you for GitHub's API, allowing for greater primary request limit.

![WhatsApp Image 2024-10-02 at 21 06 46_655cf3cd](https://github.com/user-attachments/assets/047ff675-71d9-4e0a-b4a7-262a3ab21619)
![image](https://github.com/user-attachments/assets/8a9e6d9f-097f-40c1-b629-1dbf80c0d286)
