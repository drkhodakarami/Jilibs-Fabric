document.addEventListener("DOMContentLoaded", function () {
    // Add only the specific annotations you want linkified:
    const targetAnnotations = ["Repository", "Discord", "Youtube", "Website"];

    // Find all potential signature elements at class level
    const targets = document.querySelectorAll(".type-signature, .class-description, section.class-description");

    targets.forEach(container => {
        targetAnnotations.forEach(name => {
            // Regex matches @Name("url") allowing arbitrary HTML tags/spans in between
            const regex = new RegExp(
                `(@(?:<[^>]+>)*${name}(?:<[^>]+>)*\\s*\\(\\s*(?:<[^>]+>)*")((?:https?:\\/\\/)[^"<\\s]+)("(?:<[^>]+>)*\\s*\\))`,
                "g"
            );

            container.innerHTML = container.innerHTML.replace(regex, (match, prefix, url, suffix) => {
                return `${prefix}<a href="${url}" target="_blank" rel="noopener noreferrer">${url}</a>${suffix}`;
            });
        });
    });
});