if (typeof jQuery !== 'undefined') {
	$(document).ready(function() {
		/**
		 * A checkbox using Bootstrap CSS's radio buttons
		 */
		$('body').on('click.checkbox','.radiocheckbox .btn.on:not(.readonly)',function() {
			var $checkbox = $(this).parent().siblings("input[type=checkbox]")
			$checkbox.attr('checked', true);
            $checkbox.change();
			$(this).addClass("btn-primary")
			$(this).siblings().removeClass("btn-primary")
            $(this).parent
		})
        $('body').on('click.checkbox','.radiocheckbox .btn.off:not(.readonly)',function() {
			var $checkbox = $(this).parent().siblings("input[type=checkbox]")
			$checkbox.attr('checked', false);
            $checkbox.change();
			$(this).addClass("btn-primary")
			$(this).siblings().removeClass("btn-primary")
		})
		
		/**
		 * A checkbox using Bootstrap CSS's buttons
		 */
        $('body').on('click.checkbox','.buttoncheckbox:not(.readonly)',function() {
			var $text = $(this).text()
			$(this).text($(this).attr('data-complete-text'))
			$(this).attr('data-complete-text', $text)
			$(this).toggleClass("btn-primary")
			$(this).removeClass("active")
			var $checkbox = $(this).siblings("input[type=checkbox]")
			$checkbox.attr('checked', !$checkbox.attr('checked'));
            $checkbox.change();
		})
	});
}
